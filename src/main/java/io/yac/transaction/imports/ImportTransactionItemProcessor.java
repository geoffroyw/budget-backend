package io.yac.transaction.imports;

import io.yac.auth.facade.AuthenticationFacade;
import io.yac.auth.user.CustomUserDetailsService;
import io.yac.bankaccount.repository.BankAccountRepository;
import io.yac.categories.domain.Category;
import io.yac.categories.repository.CategoryRepository;
import io.yac.common.domain.SupportedCurrency;
import io.yac.paymentmean.repository.PaymentMeanRepository;
import io.yac.transaction.domain.Transaction;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;

/**
 * Created by geoffroy on 25/06/2016.
 */
@Service
public class ImportTransactionItemProcessor implements ItemProcessor<CsvTransaction, Transaction> {

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final CategoryRepository categoryRepository;

    private final PaymentMeanRepository paymentMeanRepository;

    private final AuthenticationFacade authenticationFacade;

    private final BankAccountRepository bankAccountRepository;

    @Autowired
    public ImportTransactionItemProcessor(CategoryRepository categoryRepository,
                                          PaymentMeanRepository paymentMeanRepository,
                                          AuthenticationFacade authenticationFacade,
                                          BankAccountRepository bankAccountRepository) {
        this.categoryRepository = categoryRepository;
        this.paymentMeanRepository = paymentMeanRepository;
        this.authenticationFacade = authenticationFacade;
        this.bankAccountRepository = bankAccountRepository;
    }


    @Override
    public Transaction process(CsvTransaction item) throws Exception {

        Date transactionDate = Date.from(
                LocalDate.parse(item.getDate(), dateFormatter).atStartOfDay(ZoneId.systemDefault()).toInstant());

        Integer amount = null;
        if (item.getIncomeAmount() != null) {
            Double amountDouble = Double.parseDouble(item.getIncomeAmount()) * 100.0;
            amount = amountDouble.intValue();
        } else if (item.getExpenseAmount() != null) {
            Double amountDouble = Double.parseDouble(item.getExpenseAmount()) * 100.0;
            amount = -amountDouble.intValue();
        }

        CustomUserDetailsService.CurrentUser currentUser = authenticationFacade.getCurrentUser();

        Category category = categoryRepository.findOneByOwnerAndName(currentUser, item.getCategory());
        if (category == null) {
            category = Category.builder().owner(currentUser).name(item.getCategory()).build();
            categoryRepository.save(category);
        }

        return Transaction.builder().description(item.getName()).date(transactionDate).amountCents(amount)
                .isConfirmed("1".equals(item.getIsConfirmed())).settlementAmountCents(amount).currency(
                        SupportedCurrency.fromExternalName(item.getCurrency())).settlementCurrency(
                        SupportedCurrency.fromExternalName(item.getCurrency())).paymentMean(paymentMeanRepository
                        .findOneByOwnerAndName(currentUser, item.getPaymentMeanName()))
                .bankAccount(bankAccountRepository.findOneByOwnerAndName(currentUser, item.getBankAccount()))
                .categories(Collections.singletonList(category)).owner(currentUser).build();
    }

}
