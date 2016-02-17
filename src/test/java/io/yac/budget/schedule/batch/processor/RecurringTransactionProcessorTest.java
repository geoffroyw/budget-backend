package io.yac.budget.schedule.batch.processor;

import io.yac.auth.user.model.User;
import io.yac.budget.domain.BankAccount;
import io.yac.budget.domain.PaymentMean;
import io.yac.budget.domain.RecurringTransaction;
import io.yac.budget.repository.RecurringTransactionRepository;
import io.yac.budget.schedule.temporal.expression.TemporalExpression.TemporalExpressionType;
import org.junit.Test;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Created by geoffroy on 17/02/2016.
 */
public class RecurringTransactionProcessorTest {

    RecurringTransactionProcessor recurringTransactionProcessor = new RecurringTransactionProcessor(mock(
            RecurringTransactionRepository.class));

    @Test
    public void bankAccount_maps_to_recurring_transaction_bankAccount() throws Exception {
        BankAccount bankAccount = BankAccount.builder().name("name").build();
        RecurringTransaction recurringTransaction = prototypeRecurringTransactionOccuringEveryDay().bankAccount(
                bankAccount).build();

        assertThat(recurringTransactionProcessor.process(recurringTransaction).getBankAccount(), is(bankAccount));
    }

    private RecurringTransaction.Builder prototypeRecurringTransactionOccuringEveryDay() {
        return RecurringTransaction.builder().temporalExpressionType(TemporalExpressionType.DAILY);
    }

    @Test
    public void paymentMean_maps_to_recurring_transaction_paymentMean() throws Exception {
        PaymentMean paymentMean = PaymentMean.builder().name("name").build();
        RecurringTransaction recurringTransaction = prototypeRecurringTransactionOccuringEveryDay().paymentMean(
                paymentMean).build();

        assertThat(recurringTransactionProcessor.process(recurringTransaction).getPaymentMean(), is(paymentMean));
    }

    @Test
    public void amountCents_maps_to_recurring_transaction_amountCents() throws Exception {
        RecurringTransaction recurringTransaction =
                prototypeRecurringTransactionOccuringEveryDay().amountCents(1000).build();

        assertThat(recurringTransactionProcessor.process(recurringTransaction).getAmountCents(), is(1000));
    }


    @Test
    public void currency_maps_to_recurring_transaction_currency() throws Exception {
        RecurringTransaction recurringTransaction =
                prototypeRecurringTransactionOccuringEveryDay().currency("EUR").build();

        assertThat(recurringTransactionProcessor.process(recurringTransaction).getCurrency(), is("EUR"));
    }

    @Test
    public void description_maps_to_recurring_transaction_description() throws Exception {
        RecurringTransaction recurringTransaction =
                prototypeRecurringTransactionOccuringEveryDay().description("description").build();

        assertThat(recurringTransactionProcessor.process(recurringTransaction).getDescription(), is("description"));
    }

    @Test
    public void owner_maps_to_recurring_transaction_owner() throws Exception {
        User owner = User.builder().login("login").build();
        RecurringTransaction recurringTransaction = prototypeRecurringTransactionOccuringEveryDay().owner(
                owner).build();

        assertThat(recurringTransactionProcessor.process(recurringTransaction).getOwner(), is(owner));
    }

    @Test
    public void is_confirmed_is_false() throws Exception {
        RecurringTransaction recurringTransaction = prototypeRecurringTransactionOccuringEveryDay().build();

        assertThat(recurringTransactionProcessor.process(recurringTransaction).isConfirmed(), is(false));
    }


    @Test
    public void transactionDate_is_today() throws Exception {
        RecurringTransaction recurringTransaction = prototypeRecurringTransactionOccuringEveryDay().build();

        Instant now = Instant.now();

        Instant transactionInstant =
                Instant.ofEpochMilli(recurringTransactionProcessor.process(recurringTransaction).getDate().getTime());
        assertThat(transactionInstant.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_DATE),
                is(now.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_DATE)));
    }


    @Test
    public void recurringTransaction_last_run_on_is_today() throws Exception {
        RecurringTransaction recurringTransaction = prototypeRecurringTransactionOccuringEveryDay().build();

        Instant now = Instant.now();

        recurringTransactionProcessor.process(recurringTransaction);

        Instant recurringTransactionLastRunOnInstant =
                Instant.ofEpochMilli(recurringTransaction.getLastRunOn().getTime());
        assertThat(
                recurringTransactionLastRunOnInstant.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_DATE),
                is(now.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_DATE)));
    }

    @Test
    public void recurringTransaction_returns_null_if_transaction_is_not_occuring_today() throws Exception {
        RecurringTransaction recurringTransaction = RecurringTransaction.builder().build();

        recurringTransactionProcessor.process(recurringTransaction);

        assertThat(
                recurringTransactionProcessor.process(recurringTransaction),
                is(nullValue()));
    }
}