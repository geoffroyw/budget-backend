package io.yac.budget.api.endpoint;

import io.katharsis.resource.exception.ResourceNotFoundException;
import io.yac.Application;
import io.yac.auth.facade.AuthenticationFacade;
import io.yac.auth.user.CustomUserDetailsService;
import io.yac.auth.user.model.User;
import io.yac.budget.api.converter.impl.TransactionConverter;
import io.yac.budget.api.factory.BankAccountFactory;
import io.yac.budget.api.factory.PaymentMeanFactory;
import io.yac.budget.api.factory.TransactionFactory;
import io.yac.budget.api.factory.UserFactory;
import io.yac.budget.api.resources.BankAccountResource;
import io.yac.budget.api.resources.PaymentMeanResource;
import io.yac.budget.api.resources.TransactionResource;
import io.yac.budget.domain.SupportedCurrency;
import io.yac.budget.domain.Transaction;
import io.yac.budget.repository.TransactionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Arrays;
import java.util.Date;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by geoffroy on 14/02/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(classes = Application.class)
public class TransactionEndpointTest {


    @Autowired
    TransactionConverter transactionConverter;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    UserFactory userFactory;

    @Autowired
    TransactionFactory transactionFactory;

    @Autowired
    PaymentMeanFactory paymentMeanFactory;

    @Autowired
    BankAccountFactory bankAccountFactory;


    @Test
    public void findOne_returns_the_resource_with_the_given_id() throws Exception {
        User user = userFactory.getOrCreateUser("login1");
        Transaction transaction = transactionFactory.saveTransaction(user);

        AuthenticationFacade dummy_authentication_facade = mock(AuthenticationFacade.class);
        when(dummy_authentication_facade.getCurrentUser()).thenReturn(new CustomUserDetailsService.CurrentUser(user));
        TransactionEndpoint transactionEndpoint =
                new TransactionEndpoint(transactionRepository, dummy_authentication_facade,
                        transactionConverter);

        assertThat(transactionEndpoint.findOne(transaction.getId(), null).getId(), is(transaction.getId()));

    }


    @Test(expected = ResourceNotFoundException.class)
    public void findOne_throws_resource_not_found_exception_if_no_resource_with_the_given_id_exists()
            throws Exception {
        Long unexistingId = -20L;

        User user = userFactory.getOrCreateUser("login1");
        AuthenticationFacade dummy_authentication_facade = mock(AuthenticationFacade.class);
        when(dummy_authentication_facade.getCurrentUser()).thenReturn(new CustomUserDetailsService.CurrentUser(user));
        TransactionEndpoint transactionEndpoint =
                new TransactionEndpoint(transactionRepository, dummy_authentication_facade,
                        transactionConverter);

        transactionEndpoint.findOne(unexistingId, null);

    }

    @Test(expected = ResourceNotFoundException.class)
    public void findOne_throws_resource_not_found_exception_if_resource_with_the_given_id_exists_but_belongs_to_another_user()
            throws Exception {
        User user1 = userFactory.getOrCreateUser("login1");
        User user2 = userFactory.getOrCreateUser("login2");

        Transaction transaction_of_user1 = transactionFactory.saveTransaction(user1);

        AuthenticationFacade dummy_authentication_facade = mock(AuthenticationFacade.class);
        when(dummy_authentication_facade.getCurrentUser()).thenReturn(new CustomUserDetailsService.CurrentUser(user2));

        TransactionEndpoint transactionEndpoint =
                new TransactionEndpoint(transactionRepository, dummy_authentication_facade,
                        transactionConverter);

        transactionEndpoint.findOne(transaction_of_user1.getId(), null);

    }

    @Test
    public void save_sets_the_owner() {
        User user = userFactory.getOrCreateUser("login1");

        AuthenticationFacade dummy_authentication_facade = mock(AuthenticationFacade.class);
        when(dummy_authentication_facade.getCurrentUser()).thenReturn(new CustomUserDetailsService.CurrentUser(user));

        TransactionResource resource =
                TransactionResource.builder().currency(SupportedCurrency.EUR.getExternalName()).amountCents(100)
                        .isConfirmed(false)
                        .date(new Date())
                        .paymentMean(
                                PaymentMeanResource.builder().id(paymentMeanFactory.savePaymentMean(user).getId())
                                        .build())
                        .bankAccount(BankAccountResource.builder().id(bankAccountFactory.saveBankAccount(user).getId())
                                .build())
                        .description("any").build();

        TransactionEndpoint transactionEndpoint =
                new TransactionEndpoint(transactionRepository, dummy_authentication_facade,
                        transactionConverter);

        Long savedTransactionId = transactionEndpoint.save(resource).getId();
        assertThat(transactionRepository.findOne(savedTransactionId).getOwner().getId(), is(user.getId()));


    }


    @Test
    public void delete_deletes_the_resource_with_the_given_id() throws Exception {
        User user = userFactory.getOrCreateUser("login1");
        Transaction transaction =
                transactionFactory.saveTransaction(user);


        AuthenticationFacade dummy_authentication_facade = mock(AuthenticationFacade.class);
        when(dummy_authentication_facade.getCurrentUser()).thenReturn(new CustomUserDetailsService.CurrentUser(user));
        TransactionEndpoint transactionEndpoint =
                new TransactionEndpoint(transactionRepository, dummy_authentication_facade,
                        transactionConverter);

        Long entityId = transaction.getId();
        transactionEndpoint.delete(entityId);
        assertThat(transactionRepository.findOne(entityId), is(nullValue()));

    }

    @Test(expected = ResourceNotFoundException.class)
    public void deletes_throws_resource_not_found_exception_if_no_resource_with_the_given_id_exists()
            throws Exception {
        Long unexistingId = -20L;

        User user = userFactory.getOrCreateUser("login1");
        AuthenticationFacade dummy_authentication_facade = mock(AuthenticationFacade.class);
        when(dummy_authentication_facade.getCurrentUser()).thenReturn(new CustomUserDetailsService.CurrentUser(user));
        TransactionEndpoint transactionEndpoint =
                new TransactionEndpoint(transactionRepository, dummy_authentication_facade,
                        transactionConverter);

        transactionEndpoint.delete(unexistingId);

    }

    @Test(expected = ResourceNotFoundException.class)
    public void deletes_throws_resource_not_found_exception_if_resource_belongs_to_another_user()
            throws Exception {
        User user1 = userFactory.getOrCreateUser("login1");
        User user2 = userFactory.getOrCreateUser("login2");

        Transaction transaction_of_user_1 = transactionFactory.saveTransaction(user1);

        AuthenticationFacade dummy_authentication_facade = mock(AuthenticationFacade.class);
        when(dummy_authentication_facade.getCurrentUser()).thenReturn(new CustomUserDetailsService.CurrentUser(user2));

        TransactionEndpoint transactionEndpoint =
                new TransactionEndpoint(transactionRepository, dummy_authentication_facade,
                        transactionConverter);

        Long entityId = transaction_of_user_1.getId();
        transactionEndpoint.delete(entityId);

    }


    @Test
    public void findAll_returns_all_the_bank_transactions_of_the_current_user()
            throws Exception {
        User user1 = userFactory.getOrCreateUser("login1");
        User currentUser = userFactory.getOrCreateUser("login2");

        Transaction transaction_of_user_1 = transactionFactory.saveTransaction(user1);
        Transaction transaction_of_current_user = transactionFactory.saveTransaction(currentUser);


        AuthenticationFacade dummy_authentication_facade = mock(AuthenticationFacade.class);
        when(dummy_authentication_facade.getCurrentUser())
                .thenReturn(new CustomUserDetailsService.CurrentUser(currentUser));

        TransactionEndpoint transactionEndpoint =
                new TransactionEndpoint(transactionRepository, dummy_authentication_facade,
                        transactionConverter);

        assertThat(transactionEndpoint.findAll(null),
                is(allOf(
                        hasItem(transactionConverter.convertToResource(transaction_of_current_user)),
                        not(hasItem(transactionConverter.convertToResource(transaction_of_user_1))))));
    }

    @Test
    public void findAll_ids_returns_all_the_bank_transactions_of_the_current_user()
            throws Exception {
        User user1 = userFactory.getOrCreateUser("login1");
        User currentUser = userFactory.getOrCreateUser("login2");

        Transaction transaction_of_user_1 = transactionFactory.saveTransaction(user1);
        Transaction transaction_of_current_user = transactionFactory.saveTransaction(currentUser);


        AuthenticationFacade dummy_authentication_facade = mock(AuthenticationFacade.class);
        when(dummy_authentication_facade.getCurrentUser())
                .thenReturn(new CustomUserDetailsService.CurrentUser(currentUser));

        TransactionEndpoint transactionEndpoint =
                new TransactionEndpoint(transactionRepository, dummy_authentication_facade,
                        transactionConverter);

        assertThat(
                transactionEndpoint
                        .findAll(Arrays.asList(transaction_of_user_1.getId(), transaction_of_current_user.getId()),
                                null),
                is(allOf(
                        hasItem(transactionConverter.convertToResource(transaction_of_current_user)),
                        not(hasItem(transactionConverter.convertToResource(transaction_of_user_1))))));


    }
}