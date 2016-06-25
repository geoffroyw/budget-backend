package io.yac.transaction.imports;

import io.yac.auth.facade.AuthenticationFacade;
import io.yac.auth.user.CustomUserDetailsService.CurrentUser;
import io.yac.auth.user.model.User;
import io.yac.bankaccount.domain.BankAccount;
import io.yac.bankaccount.repository.BankAccountRepository;
import io.yac.categories.domain.Category;
import io.yac.categories.repository.CategoryRepository;
import io.yac.common.domain.SupportedCurrency;
import io.yac.paymentmean.domain.PaymentMean;
import io.yac.paymentmean.repository.PaymentMeanRepository;
import org.junit.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Created by geoffroy on 25/06/2016.
 */
public class ImportTransactionItemProcessorTest {

    private PaymentMeanRepository dummyPaymentMeanRepository = mock(PaymentMeanRepository.class);

    private CategoryRepository dummyCategoryRepository = mock(CategoryRepository.class);

    private AuthenticationFacade dummyAuthenticationFacade = mock(AuthenticationFacade.class);

    private BankAccountRepository dummyBankAccountRepository = mock(BankAccountRepository.class);


    @Test
    public void transaction_description_is_csv_transaction_name() throws Exception {
        CsvTransaction csv = CsvTransaction.builder().date("01.01.2016").name("transaction name").build();

        ImportTransactionItemProcessor processor =
                new ImportTransactionItemProcessor(dummyCategoryRepository, dummyPaymentMeanRepository,
                        dummyAuthenticationFacade, dummyBankAccountRepository);

        assertThat(processor.process(csv).getDescription(), is("transaction name"));
    }

    @Test
    public void transaction_date_is_csv_transaction_date_parsed_as_dd_DOT_mm_DOT_yyyy() throws Exception {
        Date transactionDate = Date.from(LocalDate.of(2016, 12, 24).atStartOfDay(ZoneId.systemDefault()).toInstant());

        CsvTransaction csv = CsvTransaction.builder().date("24.12.2016").build();

        ImportTransactionItemProcessor processor =
                new ImportTransactionItemProcessor(dummyCategoryRepository, dummyPaymentMeanRepository,
                        dummyAuthenticationFacade, dummyBankAccountRepository);

        assertThat(processor.process(csv).getDate(), is(transactionDate));
    }

    @Test
    public void transaction_amount_is_csv_transaction_income_amount_if_not_null() throws Exception {
        CsvTransaction csv = CsvTransaction.builder().date("01.01.2016").incomeAmount("10.63").build();

        ImportTransactionItemProcessor processor =
                new ImportTransactionItemProcessor(dummyCategoryRepository, dummyPaymentMeanRepository,
                        dummyAuthenticationFacade, dummyBankAccountRepository);


        assertThat(processor.process(csv).getAmountCents(), is(1063));
    }

    @Test
    public void transaction_amount_is_minus_csv_transaction_expense_amount_if_not_null() throws Exception {
        CsvTransaction csv = CsvTransaction.builder().date("01.01.2016").expenseAmount("8742.43").build();

        ImportTransactionItemProcessor processor =
                new ImportTransactionItemProcessor(dummyCategoryRepository, dummyPaymentMeanRepository,
                        dummyAuthenticationFacade, dummyBankAccountRepository);


        assertThat(processor.process(csv).getAmountCents(), is(-874243));
    }

    @Test
    public void transaction_setllement_amount_is_csv_transaction_income_amount_if_not_null() throws Exception {
        CsvTransaction csv = CsvTransaction.builder().date("01.01.2016").incomeAmount("10.63").build();

        ImportTransactionItemProcessor processor =
                new ImportTransactionItemProcessor(dummyCategoryRepository, dummyPaymentMeanRepository,
                        dummyAuthenticationFacade, dummyBankAccountRepository);


        assertThat(processor.process(csv).getSettlementAmountCents(), is(1063));
    }

    @Test
    public void transaction_settlement_amount_is_minus_csv_transaction_expense_amount_if_not_null() throws Exception {
        CsvTransaction csv = CsvTransaction.builder().date("01.01.2016").expenseAmount("8742.43").build();

        ImportTransactionItemProcessor processor =
                new ImportTransactionItemProcessor(dummyCategoryRepository, dummyPaymentMeanRepository,
                        dummyAuthenticationFacade, dummyBankAccountRepository);

        assertThat(processor.process(csv).getSettlementAmountCents(), is(-874243));
    }

    @Test
    public void transaction_confirmed_is_true_if_csv_transaction_confirmed_is_1() throws Exception {
        CsvTransaction csv = CsvTransaction.builder().date("01.01.2016").isConfirmed("1").build();

        ImportTransactionItemProcessor processor =
                new ImportTransactionItemProcessor(dummyCategoryRepository, dummyPaymentMeanRepository,
                        dummyAuthenticationFacade, dummyBankAccountRepository);

        assertThat(processor.process(csv).isConfirmed(), is(true));
    }

    @Test
    public void transaction_confirmed_is_false_if_csv_transaction_confirmed_is_not_1() throws Exception {
        CsvTransaction csv = CsvTransaction.builder().date("01.01.2016").isConfirmed("lkqjds").build();

        ImportTransactionItemProcessor processor =
                new ImportTransactionItemProcessor(dummyCategoryRepository, dummyPaymentMeanRepository,
                        dummyAuthenticationFacade, dummyBankAccountRepository);

        assertThat(processor.process(csv).isConfirmed(), is(false));
    }

    @Test
    public void transaction_currency_maps_to_csv_transaction_currency() throws Exception {
        CsvTransaction csv = CsvTransaction.builder().date("01.01.2016").currency("USD").build();

        ImportTransactionItemProcessor processor =
                new ImportTransactionItemProcessor(dummyCategoryRepository, dummyPaymentMeanRepository,
                        dummyAuthenticationFacade, dummyBankAccountRepository);

        assertThat(processor.process(csv).getCurrency(), is(SupportedCurrency.USD));
    }

    @Test
    public void transaction_settlement_currency_maps_to_csv_transaction_currency() throws Exception {
        CsvTransaction csv = CsvTransaction.builder().date("01.01.2016").currency("EUR").build();

        ImportTransactionItemProcessor processor =
                new ImportTransactionItemProcessor(dummyCategoryRepository, dummyPaymentMeanRepository,
                        dummyAuthenticationFacade, dummyBankAccountRepository);

        assertThat(processor.process(csv).getSettlementCurrency(), is(SupportedCurrency.EUR));
    }


    @Test
    public void transaction_owner_is_current_user() throws Exception {
        CsvTransaction csv = CsvTransaction.builder().date("01.01.2016").currency("EUR").build();

        CurrentUser currentUser = new CurrentUser(User.builder().login("foo").build());

        when(dummyAuthenticationFacade.getCurrentUser()).thenReturn(currentUser);

        ImportTransactionItemProcessor processor =
                new ImportTransactionItemProcessor(dummyCategoryRepository, dummyPaymentMeanRepository,
                        dummyAuthenticationFacade, dummyBankAccountRepository);

        assertThat(processor.process(csv).getOwner(), is(currentUser));
    }

    @Test
    public void payment_means_maps_to_the_current_user_payment_mean_with_the_csv_transaction_payment_mean_name()
            throws Exception {
        CurrentUser currentUser = new CurrentUser(User.builder().login("foo").build());
        PaymentMean matchingPaymentMean = PaymentMean.builder().name("Some name").owner(currentUser).build();

        PaymentMeanRepository dummyPaymentMeanRepository = mock(PaymentMeanRepository.class);

        AuthenticationFacade dummyAuthenticationFacade = mock(AuthenticationFacade.class);

        when(dummyAuthenticationFacade.getCurrentUser()).thenReturn(currentUser);

        when(dummyPaymentMeanRepository.findOneByOwnerAndName(currentUser, "foo")).thenReturn(matchingPaymentMean);


        CsvTransaction csv = CsvTransaction.builder().date("01.01.2016").paymentMeanName("foo").build();


        ImportTransactionItemProcessor processor =
                new ImportTransactionItemProcessor(dummyCategoryRepository, dummyPaymentMeanRepository,
                        dummyAuthenticationFacade, dummyBankAccountRepository);


        assertThat(processor.process(csv).getPaymentMean(), is(matchingPaymentMean));

    }


    @Test
    public void bankAccount_maps_to_the_current_user_bankAccount_with_the_csv_transaction_bankAccount_name()
            throws Exception {
        CurrentUser currentUser = new CurrentUser(User.builder().login("foo").build());
        BankAccount matchingBankAccount = BankAccount.builder().name("name").owner(currentUser).build();

        BankAccountRepository dummyBankAccountRepository = mock(BankAccountRepository.class);

        AuthenticationFacade dummyAuthenticationFacade = mock(AuthenticationFacade.class);

        when(dummyAuthenticationFacade.getCurrentUser()).thenReturn(currentUser);

        when(dummyBankAccountRepository.findOneByOwnerAndName(currentUser, "name")).thenReturn(matchingBankAccount);


        CsvTransaction csv = CsvTransaction.builder().date("01.01.2016").bankAccount("name").build();


        ImportTransactionItemProcessor processor =
                new ImportTransactionItemProcessor(dummyCategoryRepository, dummyPaymentMeanRepository,
                        dummyAuthenticationFacade, dummyBankAccountRepository);


        assertThat(processor.process(csv).getBankAccount(), is(matchingBankAccount));

    }

    @Test
    public void category_maps_to_the_current_user_category_with_the_csv_transaction_category_name()
            throws Exception {
        CurrentUser currentUser = new CurrentUser(User.builder().login("foo").build());
        Category matchingCategory = Category.builder().name("Some name").owner(currentUser).build();

        CategoryRepository dummyCategoryRepository = mock(CategoryRepository.class);

        AuthenticationFacade dummyAuthenticationFacade = mock(AuthenticationFacade.class);

        when(dummyAuthenticationFacade.getCurrentUser()).thenReturn(currentUser);

        when(dummyCategoryRepository.findOneByOwnerAndName(currentUser, "Some name")).thenReturn(matchingCategory);


        CsvTransaction csv = CsvTransaction.builder().date("01.01.2016").category("Some name").build();


        ImportTransactionItemProcessor processor =
                new ImportTransactionItemProcessor(dummyCategoryRepository, dummyPaymentMeanRepository,
                        dummyAuthenticationFacade, dummyBankAccountRepository);


        assertThat(processor.process(csv).getCategories().get(0), is(matchingCategory));

    }

    @Test
    public void category_maps_to_a_new_category_with_the_given_name_if_none_already_exist_for_the_current_user_with_the_csv_transaction_category_name()
            throws Exception {
        CurrentUser currentUser = new CurrentUser(User.builder().login("foo").build());
        Category newCategory = Category.builder().name("Some name").owner(currentUser).build();

        CategoryRepository dummyCategoryRepository = mock(CategoryRepository.class);

        AuthenticationFacade dummyAuthenticationFacade = mock(AuthenticationFacade.class);

        when(dummyAuthenticationFacade.getCurrentUser()).thenReturn(currentUser);

        when(dummyCategoryRepository.findOneByOwnerAndName(currentUser, "Some name")).thenReturn(null);


        CsvTransaction csv = CsvTransaction.builder().date("01.01.2016").category("Some name").build();


        ImportTransactionItemProcessor processor =
                new ImportTransactionItemProcessor(dummyCategoryRepository, dummyPaymentMeanRepository,
                        dummyAuthenticationFacade, dummyBankAccountRepository);


        assertThat(processor.process(csv).getCategories().get(0), is(newCategory));

    }

}