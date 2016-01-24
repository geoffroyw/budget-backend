package io.yac.budget.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.yac.budget.backend.api.exceptions.BankAccountNotFoundException;
import io.yac.budget.backend.api.response.RestBankAccount;
import io.yac.budget.backend.domain.entity.BankAccount;
import io.yac.budget.backend.domain.repository.BankAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by geoffroy on 24/01/2016.
 */
@RestController
@RequestMapping("/bank-accounts")
public class BankAccountController {

    private static final Logger LOG = LoggerFactory.getLogger(BankAccountController.class);
    @Autowired
    private BankAccountRepository bankAccountRepository;

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public String bankAccounts() throws JsonProcessingException {

        Iterable<BankAccount> bankAccounts = bankAccountRepository.findAll();

        List<RestBankAccount> restBankAccounts = new ArrayList<>();
        for (BankAccount bankAccount : bankAccounts) {
            restBankAccounts.add(new RestBankAccount(bankAccount));
        }

        final ObjectMapper mapper = new ObjectMapper();
        final ObjectWriter writer = mapper.writer().withRootName("bank-accounts");
        return writer.writeValueAsString(restBankAccounts);

    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json",
                    produces = "application/json")
    public RestBankAccount bankAccounts(@RequestBody RestBankAccount account) {
        return new RestBankAccount(bankAccountRepository.save(account.toBankAccount()));

    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = "application/json")
    public RestBankAccount get(@PathVariable String id) {
        BankAccount bankAccount = bankAccountRepository.findOne(Long.parseLong(id));
        if (bankAccount == null) {
            LOG.error("No bank account found for id " + id);
            throw new BankAccountNotFoundException(id);
        }
        return new RestBankAccount(bankAccount);
    }
}
