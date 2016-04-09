package io.yac.budget.api.endpoint;

import com.google.common.annotations.VisibleForTesting;
import io.yac.auth.facade.AuthenticationFacade;
import io.yac.budget.api.converter.impl.BankAccountConverter;
import io.yac.budget.api.resources.BankAccountResource;
import io.yac.budget.domain.BankAccount;
import io.yac.budget.recurring.transactions.client.exception.ResourceNotFoundException;
import io.yac.budget.repository.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by geoffroy on 08/04/2016.
 */
@RestController
@RequestMapping(value = "/api/bank-accounts")
public class BankAccountController {

    @Autowired
    BankAccountConverter bankAccountConverter;

    @Autowired
    BankAccountRepository bankAccountRepository;

    @Autowired
    AuthenticationFacade authenticationFacade;

    public BankAccountController() {
    }

    @VisibleForTesting
    BankAccountController(BankAccountRepository bankAccountRepository,
                          AuthenticationFacade authenticationFacade, BankAccountConverter bankAccountConverter) {

        this.bankAccountConverter = bankAccountConverter;
        this.bankAccountRepository = bankAccountRepository;
        this.authenticationFacade = authenticationFacade;
    }


    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
    public List<BankAccountResource> index() {

        return StreamSupport
                .stream(bankAccountRepository.findByOwner(authenticationFacade.getCurrentUser()).spliterator(), false)
                .map(bankAccount -> bankAccountConverter.convertToResource(bankAccount)).collect(
                        Collectors.toList());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public BankAccountResource get(@PathVariable("id") Long id) throws ResourceNotFoundException {
        BankAccount bankAccount =
                bankAccountRepository.findOneByOwnerAndId(authenticationFacade.getCurrentUser(), id);

        if (bankAccount == null) {
            throw new ResourceNotFoundException("No bank account found");
        }

        return bankAccountConverter
                .convertToResource(
                        bankAccount);
    }


    @RequestMapping(value = "/", method = RequestMethod.POST, produces = "application/json",
                    consumes = "application/json")
    public BankAccountResource create(@RequestBody BankAccountResource toBeCreated) {
        BankAccount bankAccount = bankAccountConverter.convertToEntity(toBeCreated);
        bankAccount.setOwner(authenticationFacade.getCurrentUser());
        bankAccountRepository.save(bankAccount);
        return bankAccountConverter.convertToResource(bankAccount);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH, produces = "application/json",
                    consumes = "application/json")
    public BankAccountResource updated(@PathVariable("id") Long id, @RequestBody BankAccountResource toBeUpdated)
            throws ResourceNotFoundException {
        if (bankAccountRepository.findOneByOwnerAndId(authenticationFacade.getCurrentUser(), id) == null) {
            throw new ResourceNotFoundException("No bank account found.");
        }

        if (!toBeUpdated.getId().equals(id)) {
            throw new ResourceNotFoundException("No bank account found.");
        }

        BankAccount bankAccount = bankAccountConverter.convertToEntity(toBeUpdated);
        bankAccount.setOwner(authenticationFacade.getCurrentUser());
        bankAccountRepository.save(bankAccount);
        return bankAccountConverter.convertToResource(bankAccount);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) throws ResourceNotFoundException {
        BankAccount bankAccount =
                bankAccountRepository.findOneByOwnerAndId(authenticationFacade.getCurrentUser(), id);

        if (bankAccount == null) {
            throw new ResourceNotFoundException("No bank account found.");
        }

        bankAccountRepository.delete(bankAccount);

    }

}