package io.yac.budget.schedule.batch.processor;

import io.yac.auth.user.model.User;
import io.yac.budget.domain.BankAccount;
import io.yac.budget.domain.PaymentMean;
import io.yac.budget.domain.RecurringTransaction;
import org.junit.Test;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by geoffroy on 17/02/2016.
 */
public class RecurringTransactionProcessorTest {

    RecurringTransactionProcessor recurringTransactionProcessor = new RecurringTransactionProcessor();

    @Test
    public void bankAccount_maps_to_recurring_transaction_bankAccount() throws Exception {
        BankAccount bankAccount = BankAccount.builder().name("name").build();
        RecurringTransaction recurringTransaction = RecurringTransaction.builder().bankAccount(
                bankAccount).build();

        assertThat(recurringTransactionProcessor.process(recurringTransaction).getBankAccount(), is(bankAccount));
    }

    @Test
    public void paymentMean_maps_to_recurring_transaction_paymentMean() throws Exception {
        PaymentMean paymentMean = PaymentMean.builder().name("name").build();
        RecurringTransaction recurringTransaction = RecurringTransaction.builder().paymentMean(
                paymentMean).build();

        assertThat(recurringTransactionProcessor.process(recurringTransaction).getPaymentMean(), is(paymentMean));
    }

    @Test
    public void amountCents_maps_to_recurring_transaction_amountCents() throws Exception {
        RecurringTransaction recurringTransaction = RecurringTransaction.builder().amountCents(1000).build();

        assertThat(recurringTransactionProcessor.process(recurringTransaction).getAmountCents(), is(1000));
    }


    @Test
    public void currency_maps_to_recurring_transaction_currency() throws Exception {
        RecurringTransaction recurringTransaction = RecurringTransaction.builder().currency("EUR").build();

        assertThat(recurringTransactionProcessor.process(recurringTransaction).getCurrency(), is("EUR"));
    }

    @Test
    public void description_maps_to_recurring_transaction_description() throws Exception {
        RecurringTransaction recurringTransaction = RecurringTransaction.builder().description("description").build();

        assertThat(recurringTransactionProcessor.process(recurringTransaction).getDescription(), is("description"));
    }

    @Test
    public void owner_maps_to_recurring_transaction_owner() throws Exception {
        User owner = User.builder().login("login").build();
        RecurringTransaction recurringTransaction = RecurringTransaction.builder().owner(
                owner).build();

        assertThat(recurringTransactionProcessor.process(recurringTransaction).getOwner(), is(owner));
    }

    @Test
    public void is_confirmed_is_false() throws Exception {
        RecurringTransaction recurringTransaction = RecurringTransaction.builder().build();

        assertThat(recurringTransactionProcessor.process(recurringTransaction).isConfirmed(), is(false));
    }


    @Test
    public void transactionDate_is_today() throws Exception {
        RecurringTransaction recurringTransaction = RecurringTransaction.builder().build();

        Instant now = Instant.now();

        Instant transactionInstant =
                Instant.ofEpochMilli(recurringTransactionProcessor.process(recurringTransaction).getDate().getTime());
        assertThat(transactionInstant.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_DATE),
                is(now.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_DATE)));
    }


    @Test
    public void recurringTransaction_last_run_on_is_today() throws Exception {
        RecurringTransaction recurringTransaction = RecurringTransaction.builder().build();

        Instant now = Instant.now();

        recurringTransactionProcessor.process(recurringTransaction);

        Instant recurringTransactionLastRunOnInstant =
                Instant.ofEpochMilli(recurringTransaction.getLastRunOn().getTime());
        assertThat(
                recurringTransactionLastRunOnInstant.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_DATE),
                is(now.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_DATE)));
    }
}