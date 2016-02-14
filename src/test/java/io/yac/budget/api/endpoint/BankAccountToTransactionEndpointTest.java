package io.yac.budget.api.endpoint;

import io.katharsis.resource.exception.ResourceNotFoundException;
import io.yac.Application;
import io.yac.auth.user.model.User;
import io.yac.budget.api.converter.impl.BankAccountConverter;
import io.yac.budget.api.converter.impl.TransactionConverter;
import io.yac.budget.api.factory.BankAccountFactory;
import io.yac.budget.api.factory.DummyAuthenticationFacade;
import io.yac.budget.api.factory.TransactionFactory;
import io.yac.budget.api.factory.UserFactory;
import io.yac.budget.domain.BankAccount;
import io.yac.budget.domain.Transaction;
import io.yac.budget.repository.BankAccountRepository;
import io.yac.budget.repository.TransactionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Collections;

/**
 * Created by geoffroy on 14/02/2016.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(classes = Application.class)
public class BankAccountToTransactionEndpointTest {

    @Autowired
    BankAccountRepository bankAccountRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    TransactionConverter transactionConverter;

    @Autowired
    BankAccountConverter bankAccountConverter;

    @Autowired
    UserFactory userFactory;

    @Autowired
    BankAccountFactory bankAccountFactory;

    @Autowired
    TransactionFactory transactionFactory;


    @Test(expected = ResourceNotFoundException.class)
    public void setRelations_throws_resourceNotFound_exception_if_bank_account_does_not_belongs_to_user() {
        User user1 = userFactory.getOrCreateUser("user1");
        User user2 = userFactory.getOrCreateUser("user2");

        BankAccount bankAccount_of_user2 = bankAccountFactory.saveBankAccount(user2);

        Transaction targetTransaction = transactionFactory.saveTransaction(user2);

        BankAccountToTransactionEndpoint bankAccountToTransactionEndpoint =
                new BankAccountToTransactionEndpoint(bankAccountRepository, transactionRepository, transactionConverter,
                        new DummyAuthenticationFacade(user1));

        bankAccountToTransactionEndpoint.setRelations(bankAccountConverter.convertToResource(bankAccount_of_user2),
                Collections.singletonList(targetTransaction.getId()), "");

    }


}