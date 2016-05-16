package io.yac.api.endpoint;

import com.google.common.annotations.VisibleForTesting;
import io.yac.api.converter.impl.BankAccountConverter;
import io.yac.api.exceptions.ResourceNotFoundException;
import io.yac.api.resources.BankAccountResource;
import io.yac.auth.facade.AuthenticationFacade;
import io.yac.core.domain.BankAccount;
import io.yac.core.repository.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by geoffroy on 08/04/2016.
 */
@RestController
@RequestMapping(value = "/api/bankAccounts")
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


    @RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<BankAccountResource> index() {

        return StreamSupport
                .stream(bankAccountRepository.findByOwner(authenticationFacade.getCurrentUser()).spliterator(), false)
                .map(bankAccount -> bankAccountConverter.convertToResource(bankAccount)).collect(
                        Collectors.toList());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody BankAccountResource get(@PathVariable("id") Long id) throws ResourceNotFoundException {
        BankAccount bankAccount =
                bankAccountRepository.findOneByOwnerAndId(authenticationFacade.getCurrentUser(), id);

        if (bankAccount == null) {
            throw new ResourceNotFoundException("No bank account found");
        }

        return bankAccountConverter
                .convertToResource(
                        bankAccount);
    }


    @RequestMapping(value = "", method = RequestMethod.POST, produces = "application/json",
                    consumes = "application/json")
    public @ResponseBody BankAccountResource create(@RequestBody BankAccountResource toBeCreated) {
        BankAccount bankAccount = bankAccountConverter.convertToEntity(toBeCreated, null);
        bankAccount.setOwner(authenticationFacade.getCurrentUser());
        bankAccountRepository.save(bankAccount);
        return bankAccountConverter.convertToResource(bankAccount);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = "application/json",
                    consumes = "application/json")
    public @ResponseBody BankAccountResource updated(@PathVariable("id") Long id, @RequestBody BankAccountResource toBeUpdated)
            throws ResourceNotFoundException {
        if (bankAccountRepository.findOneByOwnerAndId(authenticationFacade.getCurrentUser(), id) == null) {
            throw new ResourceNotFoundException("No bank account found.");
        }

        BankAccount bankAccount = bankAccountConverter.convertToEntity(toBeUpdated, id);
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
