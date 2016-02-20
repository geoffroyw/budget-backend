package io.yac.budget.api.endpoint;

import io.katharsis.resource.exception.ResourceNotFoundException;
import io.yac.Application;
import io.yac.auth.facade.AuthenticationFacade;
import io.yac.auth.user.CustomUserDetailsService.CurrentUser;
import io.yac.auth.user.model.User;
import io.yac.budget.api.converter.impl.BankAccountConverter;
import io.yac.budget.api.factory.BankAccountFactory;
import io.yac.budget.api.factory.UserFactory;
import io.yac.budget.api.resources.BankAccountResource;
import io.yac.budget.domain.BankAccount;
import io.yac.budget.domain.SupportedCurrency;
import io.yac.budget.repository.BankAccountRepository;
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
public class BankAccountEndpointTest {

    @Autowired
    BankAccountConverter bankAccountConverter;

    @Autowired
    BankAccountRepository bankAccountRepository;

    @Autowired
    UserFactory userFactory;

    @Autowired
    BankAccountFactory bankAccountFactory;


    @Test
    public void findOne_returns_the_resource_with_the_given_id() throws Exception {
        User user = userFactory.getOrCreateUser("login1");
        BankAccount account =
                bankAccountFactory.saveBankAccount(user);


        AuthenticationFacade dummy_authentication_facade = mock(AuthenticationFacade.class);
        when(dummy_authentication_facade.getCurrentUser()).thenReturn(new CurrentUser(user));
        BankAccountEndpoint bankAccountEndpoint =
                new BankAccountEndpoint(bankAccountRepository, dummy_authentication_facade,
                        bankAccountConverter);

        assertThat(bankAccountEndpoint.findOne(account.getId(), null).getId(), is(account.getId()));

    }

    @Test(expected = ResourceNotFoundException.class)
    public void findOne_throws_resource_not_found_exception_if_no_resource_with_the_given_id_exists()
            throws Exception {
        Long unexistingId = -20L;

        User user = userFactory.getOrCreateUser("login1");
        AuthenticationFacade dummy_authentication_facade = mock(AuthenticationFacade.class);
        when(dummy_authentication_facade.getCurrentUser()).thenReturn(new CurrentUser(user));
        BankAccountEndpoint bankAccountEndpoint =
                new BankAccountEndpoint(bankAccountRepository, dummy_authentication_facade,
                        bankAccountConverter);

        bankAccountEndpoint.findOne(unexistingId, null);

    }

    @Test(expected = ResourceNotFoundException.class)
    public void findOne_throws_resource_not_found_exception_if_resource_with_the_given_id_exists_but_belongs_to_another_user()
            throws Exception {
        User user1 = userFactory.getOrCreateUser("login1");
        User user2 = userFactory.getOrCreateUser("login2");

        BankAccount account_of_user_1 = bankAccountFactory.saveBankAccount(user1);

        AuthenticationFacade dummy_authentication_facade = mock(AuthenticationFacade.class);
        when(dummy_authentication_facade.getCurrentUser()).thenReturn(new CurrentUser(user2));

        BankAccountEndpoint bankAccountEndpoint =
                new BankAccountEndpoint(bankAccountRepository, dummy_authentication_facade,
                        bankAccountConverter);

        bankAccountEndpoint.findOne(account_of_user_1.getId(), null);

    }

    @Test
    public void save_sets_the_owner() {
        User user = userFactory.getOrCreateUser("login1");

        AuthenticationFacade dummy_authentication_facade = mock(AuthenticationFacade.class);
        when(dummy_authentication_facade.getCurrentUser()).thenReturn(new CurrentUser(user));

        BankAccountResource resource =
                BankAccountResource.builder().currency(SupportedCurrency.AUD.getExternalName()).name("any").build();

        BankAccountEndpoint bankAccountEndpoint =
                new BankAccountEndpoint(bankAccountRepository, dummy_authentication_facade,
                        bankAccountConverter);

        Long savedBankAccountId = bankAccountEndpoint.save(resource).getId();
        assertThat(bankAccountRepository.findOne(savedBankAccountId).getOwner().getId(), is(user.getId()));


    }


    @Test
    public void delete_deletes_the_resource_with_the_given_id() throws Exception {
        User user = userFactory.getOrCreateUser("login1");
        BankAccount account =
                bankAccountFactory.saveBankAccount(user);

        AuthenticationFacade dummy_authentication_facade = mock(AuthenticationFacade.class);
        when(dummy_authentication_facade.getCurrentUser()).thenReturn(new CurrentUser(user));
        BankAccountEndpoint bankAccountEndpoint =
                new BankAccountEndpoint(bankAccountRepository, dummy_authentication_facade,
                        bankAccountConverter);

        Long entityId = account.getId();
        bankAccountEndpoint.delete(entityId);
        assertThat(bankAccountRepository.findOne(entityId), is(nullValue()));

    }

    @Test(expected = ResourceNotFoundException.class)
    public void deletes_throws_resource_not_found_exception_if_no_resource_with_the_given_id_exists()
            throws Exception {
        Long unexistingId = -20L;

        User user = userFactory.getOrCreateUser("login1");
        AuthenticationFacade dummy_authentication_facade = mock(AuthenticationFacade.class);
        when(dummy_authentication_facade.getCurrentUser()).thenReturn(new CurrentUser(user));
        BankAccountEndpoint bankAccountEndpoint =
                new BankAccountEndpoint(bankAccountRepository, dummy_authentication_facade,
                        bankAccountConverter);

        bankAccountEndpoint.delete(unexistingId);

    }

    @Test(expected = ResourceNotFoundException.class)
    public void deletes_throws_resource_not_found_exception_if_resource_belongs_to_another_user()
            throws Exception {
        User user1 = userFactory.getOrCreateUser("login1");
        User user2 = userFactory.getOrCreateUser("login2");

        BankAccount account_of_user_1 = bankAccountFactory.saveBankAccount(user1);

        AuthenticationFacade dummy_authentication_facade = mock(AuthenticationFacade.class);
        when(dummy_authentication_facade.getCurrentUser()).thenReturn(new CurrentUser(user2));

        BankAccountEndpoint bankAccountEndpoint =
                new BankAccountEndpoint(bankAccountRepository, dummy_authentication_facade,
                        bankAccountConverter);

        Long entityId = account_of_user_1.getId();
        bankAccountEndpoint.delete(entityId);

    }


    @Test
    public void findAll_returns_all_the_bank_accounts_of_the_current_user()
            throws Exception {
        User user1 = userFactory.getOrCreateUser("login1");
        User currentUser = userFactory.getOrCreateUser("login2");

        BankAccount account_of_user_1 = bankAccountFactory.saveBankAccount(user1);
        BankAccount account_of_current_user = bankAccountFactory.saveBankAccount(currentUser);

        AuthenticationFacade dummy_authentication_facade = mock(AuthenticationFacade.class);
        when(dummy_authentication_facade.getCurrentUser()).thenReturn(new CurrentUser(currentUser));

        BankAccountEndpoint bankAccountEndpoint =
                new BankAccountEndpoint(bankAccountRepository, dummy_authentication_facade,
                        bankAccountConverter);

        assertThat(bankAccountEndpoint.findAll(null),
                is(allOf(
                        hasItem(bankAccountConverter.convertToResource(account_of_current_user)),
                        not(hasItem(bankAccountConverter.convertToResource(account_of_user_1))))));
    }

    @Test
    public void findAll_ids_returns_all_the_bank_accounts_of_the_current_user()
            throws Exception {
        User user1 = userFactory.getOrCreateUser("login1");
        User currentUser = userFactory.getOrCreateUser("login2");

        BankAccount account_of_user_1 = bankAccountFactory.saveBankAccount(user1);
        BankAccount account_of_current_user =
                bankAccountFactory.saveBankAccount(currentUser);

        AuthenticationFacade dummy_authentication_facade = mock(AuthenticationFacade.class);
        when(dummy_authentication_facade.getCurrentUser()).thenReturn(new CurrentUser(currentUser));

        BankAccountEndpoint bankAccountEndpoint =
                new BankAccountEndpoint(bankAccountRepository, dummy_authentication_facade,
                        bankAccountConverter);

        assertThat(
                bankAccountEndpoint
                        .findAll(Arrays.asList(account_of_user_1.getId(), account_of_current_user.getId()), null),
                is(allOf(
                        hasItem(bankAccountConverter.convertToResource(account_of_current_user)),
                        not(hasItem(bankAccountConverter.convertToResource(account_of_user_1))))));


    }
}