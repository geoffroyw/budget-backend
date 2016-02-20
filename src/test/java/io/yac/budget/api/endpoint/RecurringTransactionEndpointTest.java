package io.yac.budget.api.endpoint;

import io.katharsis.resource.exception.ResourceNotFoundException;
import io.yac.Application;
import io.yac.auth.facade.AuthenticationFacade;
import io.yac.auth.user.CustomUserDetailsService;
import io.yac.auth.user.model.User;
import io.yac.budget.api.converter.impl.RecurringTransactionConverter;
import io.yac.budget.api.factory.BankAccountFactory;
import io.yac.budget.api.factory.PaymentMeanFactory;
import io.yac.budget.api.factory.RecurringTransactionFactory;
import io.yac.budget.api.factory.UserFactory;
import io.yac.budget.api.resources.BankAccountResource;
import io.yac.budget.api.resources.PaymentMeanResource;
import io.yac.budget.api.resources.RecurringTransactionResource;
import io.yac.budget.domain.RecurringTransaction;
import io.yac.budget.domain.SupportedCurrency;
import io.yac.budget.repository.RecurringTransactionRepository;
import io.yac.budget.schedule.temporal.expression.TemporalExpression;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Arrays;

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
public class RecurringTransactionEndpointTest {


    @Autowired
    RecurringTransactionConverter recurringTransactionConverter;

    @Autowired
    RecurringTransactionRepository recurringTransactionRepository;

    @Autowired
    UserFactory userFactory;

    @Autowired
    RecurringTransactionFactory recurringTransactionFactory;

    @Autowired
    PaymentMeanFactory paymentMeanFactory;

    @Autowired
    BankAccountFactory bankAccountFactory;


    @Test
    public void findOne_returns_the_resource_with_the_given_id() throws Exception {
        User user = userFactory.getOrCreateUser("login1");
        RecurringTransaction recurringTransaction = recurringTransactionFactory.saveRecurringTransaction(user);

        AuthenticationFacade dummy_authentication_facade = mock(AuthenticationFacade.class);
        when(dummy_authentication_facade.getCurrentUser()).thenReturn(new CustomUserDetailsService.CurrentUser(user));
        RecurringTransactionEndpoint recurringTransactionEndpoint =
                new RecurringTransactionEndpoint(recurringTransactionRepository, dummy_authentication_facade,
                        recurringTransactionConverter);

        assertThat(recurringTransactionEndpoint.findOne(recurringTransaction.getId(), null).getId(),
                is(recurringTransaction.getId()));

    }


    @Test(expected = ResourceNotFoundException.class)
    public void findOne_throws_resource_not_found_exception_if_no_resource_with_the_given_id_exists()
            throws Exception {
        Long unexistingId = -20L;

        User user = userFactory.getOrCreateUser("login1");
        AuthenticationFacade dummy_authentication_facade = mock(AuthenticationFacade.class);
        when(dummy_authentication_facade.getCurrentUser()).thenReturn(new CustomUserDetailsService.CurrentUser(user));
        RecurringTransactionEndpoint recurringTransactionEndpoint =
                new RecurringTransactionEndpoint(recurringTransactionRepository, dummy_authentication_facade,
                        recurringTransactionConverter);

        recurringTransactionEndpoint.findOne(unexistingId, null);

    }

    @Test(expected = ResourceNotFoundException.class)
    public void findOne_throws_resource_not_found_exception_if_resource_with_the_given_id_exists_but_belongs_to_another_user()
            throws Exception {
        User user1 = userFactory.getOrCreateUser("login1");
        User user2 = userFactory.getOrCreateUser("login2");

        RecurringTransaction recurringTransaction_of_user1 =
                recurringTransactionFactory.saveRecurringTransaction(user1);

        AuthenticationFacade dummy_authentication_facade = mock(AuthenticationFacade.class);
        when(dummy_authentication_facade.getCurrentUser()).thenReturn(new CustomUserDetailsService.CurrentUser(user2));

        RecurringTransactionEndpoint recurringTransactionEndpoint =
                new RecurringTransactionEndpoint(recurringTransactionRepository, dummy_authentication_facade,
                        recurringTransactionConverter);

        recurringTransactionEndpoint.findOne(recurringTransaction_of_user1.getId(), null);

    }

    @Test
    public void save_sets_the_owner() {
        User user = userFactory.getOrCreateUser("login1");

        AuthenticationFacade dummy_authentication_facade = mock(AuthenticationFacade.class);
        when(dummy_authentication_facade.getCurrentUser()).thenReturn(new CustomUserDetailsService.CurrentUser(user));

        RecurringTransactionResource resource =
                RecurringTransactionResource.builder().currency(SupportedCurrency.BGN.getExternalName())
                        .amountCents(100).recurringType(
                        TemporalExpression.TemporalExpressionType.DAILY.getExternalName())
                        .paymentMean(
                                PaymentMeanResource.builder().id(paymentMeanFactory.savePaymentMean(user).getId())
                                        .build())
                        .bankAccount(BankAccountResource.builder().id(bankAccountFactory.saveBankAccount(user).getId())
                                .build())
                        .description("any").build();

        RecurringTransactionEndpoint recurringTransactionEndpoint =
                new RecurringTransactionEndpoint(recurringTransactionRepository, dummy_authentication_facade,
                        recurringTransactionConverter);

        Long savedRecurringTransactionId = recurringTransactionEndpoint.save(resource).getId();
        assertThat(recurringTransactionRepository.findOne(savedRecurringTransactionId).getOwner().getId(),
                is(user.getId()));


    }


    @Test
    public void delete_deletes_the_resource_with_the_given_id() throws Exception {
        User user = userFactory.getOrCreateUser("login1");
        RecurringTransaction recurringTransaction =
                recurringTransactionFactory.saveRecurringTransaction(user);


        AuthenticationFacade dummy_authentication_facade = mock(AuthenticationFacade.class);
        when(dummy_authentication_facade.getCurrentUser()).thenReturn(new CustomUserDetailsService.CurrentUser(user));
        RecurringTransactionEndpoint recurringTransactionEndpoint =
                new RecurringTransactionEndpoint(recurringTransactionRepository, dummy_authentication_facade,
                        recurringTransactionConverter);

        Long entityId = recurringTransaction.getId();
        recurringTransactionEndpoint.delete(entityId);
        assertThat(recurringTransactionRepository.findOne(entityId), is(nullValue()));

    }

    @Test(expected = ResourceNotFoundException.class)
    public void deletes_throws_resource_not_found_exception_if_no_resource_with_the_given_id_exists()
            throws Exception {
        Long unexistingId = -20L;

        User user = userFactory.getOrCreateUser("login1");
        AuthenticationFacade dummy_authentication_facade = mock(AuthenticationFacade.class);
        when(dummy_authentication_facade.getCurrentUser()).thenReturn(new CustomUserDetailsService.CurrentUser(user));
        RecurringTransactionEndpoint recurringTransactionEndpoint =
                new RecurringTransactionEndpoint(recurringTransactionRepository, dummy_authentication_facade,
                        recurringTransactionConverter);

        recurringTransactionEndpoint.delete(unexistingId);

    }

    @Test(expected = ResourceNotFoundException.class)
    public void deletes_throws_resource_not_found_exception_if_resource_belongs_to_another_user()
            throws Exception {
        User user1 = userFactory.getOrCreateUser("login1");
        User user2 = userFactory.getOrCreateUser("login2");

        RecurringTransaction recurringTransaction_of_user_1 =
                recurringTransactionFactory.saveRecurringTransaction(user1);

        AuthenticationFacade dummy_authentication_facade = mock(AuthenticationFacade.class);
        when(dummy_authentication_facade.getCurrentUser()).thenReturn(new CustomUserDetailsService.CurrentUser(user2));

        RecurringTransactionEndpoint recurringTransactionEndpoint =
                new RecurringTransactionEndpoint(recurringTransactionRepository, dummy_authentication_facade,
                        recurringTransactionConverter);

        Long entityId = recurringTransaction_of_user_1.getId();
        recurringTransactionEndpoint.delete(entityId);

    }


    @Test
    public void findAll_returns_all_the_bank_recurringTransactions_of_the_current_user()
            throws Exception {
        User user1 = userFactory.getOrCreateUser("login1");
        User currentUser = userFactory.getOrCreateUser("login2");

        RecurringTransaction recurringTransaction_of_user_1 =
                recurringTransactionFactory.saveRecurringTransaction(user1);
        RecurringTransaction recurringTransaction_of_current_user =
                recurringTransactionFactory.saveRecurringTransaction(currentUser);


        AuthenticationFacade dummy_authentication_facade = mock(AuthenticationFacade.class);
        when(dummy_authentication_facade.getCurrentUser())
                .thenReturn(new CustomUserDetailsService.CurrentUser(currentUser));

        RecurringTransactionEndpoint recurringTransactionEndpoint =
                new RecurringTransactionEndpoint(recurringTransactionRepository, dummy_authentication_facade,
                        recurringTransactionConverter);

        assertThat(recurringTransactionEndpoint.findAll(null),
                is(allOf(
                        hasItem(recurringTransactionConverter.convertToResource(recurringTransaction_of_current_user)),
                        not(hasItem(
                                recurringTransactionConverter.convertToResource(recurringTransaction_of_user_1))))));
    }

    @Test
    public void findAll_ids_returns_all_the_bank_recurringTransactions_of_the_current_user()
            throws Exception {
        User user1 = userFactory.getOrCreateUser("login1");
        User currentUser = userFactory.getOrCreateUser("login2");

        RecurringTransaction recurringTransaction_of_user_1 =
                recurringTransactionFactory.saveRecurringTransaction(user1);
        RecurringTransaction recurringTransaction_of_current_user =
                recurringTransactionFactory.saveRecurringTransaction(currentUser);


        AuthenticationFacade dummy_authentication_facade = mock(AuthenticationFacade.class);
        when(dummy_authentication_facade.getCurrentUser())
                .thenReturn(new CustomUserDetailsService.CurrentUser(currentUser));

        RecurringTransactionEndpoint recurringTransactionEndpoint =
                new RecurringTransactionEndpoint(recurringTransactionRepository, dummy_authentication_facade,
                        recurringTransactionConverter);

        assertThat(
                recurringTransactionEndpoint
                        .findAll(Arrays.asList(recurringTransaction_of_user_1.getId(),
                                recurringTransaction_of_current_user.getId()),
                                null),
                is(allOf(
                        hasItem(recurringTransactionConverter.convertToResource(recurringTransaction_of_current_user)),
                        not(hasItem(
                                recurringTransactionConverter.convertToResource(recurringTransaction_of_user_1))))));


    }
}