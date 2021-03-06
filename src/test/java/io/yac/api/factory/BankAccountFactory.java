package io.yac.api.factory;

import io.yac.auth.user.model.User;
import io.yac.bankaccount.domain.BankAccount;
import io.yac.common.domain.SupportedCurrency;
import io.yac.bankaccount.repository.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by geoffroy on 14/02/2016.
 */
@Component
public class BankAccountFactory {

    @Autowired
    BankAccountRepository bankAccountRepository;

    public BankAccount saveBankAccount(User owner) {
        return bankAccountRepository
                .save(BankAccount.builder().currency(SupportedCurrency.EUR).name("any").owner(owner).build());
    }

}
