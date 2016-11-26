package io.yac.bankaccount.controller;

import io.yac.Application;
import io.yac.api.factory.BankAccountFactory;
import io.yac.api.factory.UserFactory;
import io.yac.auth.facade.AuthenticationFacade;
import io.yac.auth.user.CustomUserDetailsService.CurrentUser;
import io.yac.auth.user.model.User;
import io.yac.bankaccount.domain.BankAccount;
import io.yac.bankaccount.repository.BankAccountRepository;
import io.yac.common.api.exceptions.ResourceNotFoundException;
import io.yac.common.domain.SupportedCurrency;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

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
public class BankAccountControllerTest {

    @Autowired
    private BankAccountFactory bankAccountFactory;
    @Autowired
    private BankAccountRepository bankAccountRepository;
    @Autowired
    private UserFactory userFactory;

    @Test
    public void findOne_returns_the_resource_with_the_given_id() throws Exception {
        User user = userFactory.getOrCreateUser("login1");
        BankAccount account =
                bankAccountFactory.saveBankAccount(user);


        AuthenticationFacade dummy_authentication_facade = mock(AuthenticationFacade.class);
        when(dummy_authentication_facade.getCurrentUser()).thenReturn(new CurrentUser(user));
        BankAccountController bankAccountController =
                new BankAccountController(bankAccountRepository, dummy_authentication_facade);

        assertThat(bankAccountController.get(account.getId()).getId(), is(account.getId()));

    }

    @Test(expected = ResourceNotFoundException.class)
    public void findOne_throws_resource_not_found_exception_if_no_resource_with_the_given_id_exists()
            throws Exception {
        Long unexistingId = -20L;

        User user = userFactory.getOrCreateUser("login1");
        AuthenticationFacade dummy_authentication_facade = mock(AuthenticationFacade.class);
        when(dummy_authentication_facade.getCurrentUser()).thenReturn(new CurrentUser(user));
        BankAccountController bankAccountController =
                new BankAccountController(bankAccountRepository, dummy_authentication_facade);

        bankAccountController.get(unexistingId);

    }

    @Test(expected = ResourceNotFoundException.class)
    public void findOne_throws_resource_not_found_exception_if_resource_with_the_given_id_exists_but_belongs_to_another_user()
            throws Exception {
        User user1 = userFactory.getOrCreateUser("login1");
        User user2 = userFactory.getOrCreateUser("login2");

        BankAccount account_of_user_1 = bankAccountFactory.saveBankAccount(user1);

        AuthenticationFacade dummy_authentication_facade = mock(AuthenticationFacade.class);
        when(dummy_authentication_facade.getCurrentUser()).thenReturn(new CurrentUser(user2));

        BankAccountController bankAccountController =
                new BankAccountController(bankAccountRepository, dummy_authentication_facade);

        bankAccountController.get(account_of_user_1.getId());

    }

    @Test
    public void save_sets_the_owner() {
        User user = userFactory.getOrCreateUser("login1");

        AuthenticationFacade dummy_authentication_facade = mock(AuthenticationFacade.class);
        when(dummy_authentication_facade.getCurrentUser()).thenReturn(new CurrentUser(user));

        BankAccount resource =
                BankAccount.builder().currency(SupportedCurrency.AUD).name("any").build();

        BankAccountController bankAccountController =
                new BankAccountController(bankAccountRepository, dummy_authentication_facade);

        Long savedBankAccountId = bankAccountController.create(resource).getId();
        assertThat(bankAccountRepository.findOne(savedBankAccountId).getOwner().getId(), is(user.getId()));


    }


    @Test
    public void delete_deletes_the_resource_with_the_given_id() throws Exception {
        User user = userFactory.getOrCreateUser("login1");
        BankAccount account =
                bankAccountFactory.saveBankAccount(user);

        AuthenticationFacade dummy_authentication_facade = mock(AuthenticationFacade.class);
        when(dummy_authentication_facade.getCurrentUser()).thenReturn(new CurrentUser(user));
        BankAccountController bankAccountController =
                new BankAccountController(bankAccountRepository, dummy_authentication_facade);

        Long entityId = account.getId();
        bankAccountController.delete(entityId);
        assertThat(bankAccountRepository.findOne(entityId), is(nullValue()));

    }

    @Test(expected = ResourceNotFoundException.class)
    public void deletes_throws_resource_not_found_exception_if_no_resource_with_the_given_id_exists()
            throws Exception {
        Long unexistingId = -20L;

        User user = userFactory.getOrCreateUser("login1");
        AuthenticationFacade dummy_authentication_facade = mock(AuthenticationFacade.class);
        when(dummy_authentication_facade.getCurrentUser()).thenReturn(new CurrentUser(user));
        BankAccountController bankAccountController =
                new BankAccountController(bankAccountRepository, dummy_authentication_facade);

        bankAccountController.delete(unexistingId);

    }

    @Test(expected = ResourceNotFoundException.class)
    public void deletes_throws_resource_not_found_exception_if_resource_belongs_to_another_user()
            throws Exception {
        User user1 = userFactory.getOrCreateUser("login1");
        User user2 = userFactory.getOrCreateUser("login2");

        BankAccount account_of_user_1 = bankAccountFactory.saveBankAccount(user1);

        AuthenticationFacade dummy_authentication_facade = mock(AuthenticationFacade.class);
        when(dummy_authentication_facade.getCurrentUser()).thenReturn(new CurrentUser(user2));

        BankAccountController bankAccountController =
                new BankAccountController(bankAccountRepository, dummy_authentication_facade);

        Long entityId = account_of_user_1.getId();
        bankAccountController.delete(entityId);

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

        BankAccountController bankAccountController =
                new BankAccountController(bankAccountRepository, dummy_authentication_facade);

        assertThat(bankAccountController.index(),
                is(allOf(
                        hasItem(account_of_current_user),
                        not(hasItem(account_of_user_1)))));
    }

}