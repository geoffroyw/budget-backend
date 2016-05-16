package io.yac.bankaccount.api.converter;

import com.google.common.annotations.VisibleForTesting;
import io.yac.common.api.converter.ResourceEntityConverter;
import io.yac.bankaccount.api.BankAccountResource;
import io.yac.bankaccount.domain.BankAccount;
import io.yac.common.domain.SupportedCurrency;
import io.yac.transaction.domain.Transaction;
import io.yac.bankaccount.repository.BankAccountRepository;
import io.yac.transaction.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by geoffroy on 07/02/2016.
 */
@Service
public class BankAccountConverter implements ResourceEntityConverter<BankAccountResource, BankAccount> {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    BankAccountRepository bankAccountRepository;

    BankAccountConverter() {
    }

    @VisibleForTesting
    BankAccountConverter(TransactionRepository transactionRepository, BankAccountRepository bankAccountRepository) {

        this.transactionRepository = transactionRepository;
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    public BankAccountResource convertToResource(BankAccount entity) {
        return BankAccountResource.builder().id(entity.getId()).currency(entity.getCurrency().getExternalName())
                .name(entity.getName())
                .transactions(entity.getTransactions() == null ? null :
                              entity.getTransactions().stream()
                                      .map(Transaction::getId)
                                      .collect(Collectors.toList()))
                .build();
    }

    @Override
    public BankAccount convertToEntity(BankAccountResource resource, Long id) {
        BankAccount bankAccount;
        if (id == null) {
            bankAccount = new BankAccount();
        } else {
            bankAccount = bankAccountRepository.findOne(id);
        }

        bankAccount.setCurrency(SupportedCurrency.fromExternalName(resource.getCurrency()));
        bankAccount.setName(resource.getName());
        bankAccount
                .setTransactions(resource.getTransactions() == null ? null : (List<Transaction>) transactionRepository
                        .findAll(resource.getTransactions()));


        return bankAccount;

    }
}
