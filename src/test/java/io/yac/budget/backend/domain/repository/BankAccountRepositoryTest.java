package io.yac.budget.backend.domain.repository;

import io.yac.budget.backend.BackendApplication;
import io.yac.budget.backend.domain.entity.BankAccount;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Currency;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by geoffroy on 23/01/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BackendApplication.class)
@TestPropertySource(locations = "classpath:test.properties")
public class BankAccountRepositoryTest {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Test
    public void test_create() {
        BankAccount bankAccount = BankAccount.builder().currency(Currency.getInstance("EUR")).build();

        BankAccount savedEntity = bankAccountRepository.save(bankAccount);

        assertThat(savedEntity.getId(), is(notNullValue()));
    }

    @Test
    public void test_find() {
        BankAccount bankAccount = bankAccountRepository.save(
                BankAccount.builder().currency(Currency.getInstance("EUR")).build());

        Long id = bankAccount.getId();

        assertThat(bankAccountRepository.findOne(id).getCurrency(), is(Currency.getInstance("EUR")));
    }
}