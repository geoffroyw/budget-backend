package io.yac.api.endpoint;

import io.yac.Application;
import io.yac.bankaccount.api.converter.BankAccountConverter;
import io.yac.api.exceptions.ResourceNotFoundException;
import io.yac.api.factory.BankAccountFactory;
import io.yac.api.factory.UserFactory;
import io.yac.bankaccount.api.BankAccountResource;
import io.yac.auth.facade.AuthenticationFacade;
import io.yac.auth.user.CustomUserDetailsService.CurrentUser;
import io.yac.auth.user.model.User;
import io.yac.bankaccount.api.endpoint.BankAccountController;
import io.yac.bankaccount.domain.BankAccount;
import io.yac.core.domain.SupportedCurrency;
import io.yac.bankaccount.repository.BankAccountRepository;
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
        BankAccountController bankAccountController =
                new BankAccountController(bankAccountRepository, dummy_authentication_facade,
                        bankAccountConverter);

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
                new BankAccountController(bankAccountRepository, dummy_authentication_facade,
                        bankAccountConverter);

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
                new BankAccountController(bankAccountRepository, dummy_authentication_facade,
                        bankAccountConverter);

        bankAccountController.get(account_of_user_1.getId());

    }

    @Test
    public void save_sets_the_owner() {
        User user = userFactory.getOrCreateUser("login1");

        AuthenticationFacade dummy_authentication_facade = mock(AuthenticationFacade.class);
        when(dummy_authentication_facade.getCurrentUser()).thenReturn(new CurrentUser(user));

        BankAccountResource resource =
                BankAccountResource.builder().currency(SupportedCurrency.AUD.getExternalName()).name("any").build();

        BankAccountController bankAccountController =
                new BankAccountController(bankAccountRepository, dummy_authentication_facade,
                        bankAccountConverter);

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
                new BankAccountController(bankAccountRepository, dummy_authentication_facade,
                        bankAccountConverter);

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
                new BankAccountController(bankAccountRepository, dummy_authentication_facade,
                        bankAccountConverter);

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
                new BankAccountController(bankAccountRepository, dummy_authentication_facade,
                        bankAccountConverter);

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
                new BankAccountController(bankAccountRepository, dummy_authentication_facade,
                        bankAccountConverter);

        assertThat(bankAccountController.index(),
                is(allOf(
                        hasItem(bankAccountConverter.convertToResource(account_of_current_user)),
                        not(hasItem(bankAccountConverter.convertToResource(account_of_user_1))))));
    }

}