package io.yac.bankaccount.api.endpoint;

import com.fasterxml.jackson.annotation.JsonView;
import io.yac.auth.facade.AuthenticationFacade;
import io.yac.bankaccount.domain.BankAccount;
import io.yac.bankaccount.domain.View;
import io.yac.bankaccount.repository.BankAccountRepository;
import io.yac.common.api.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by geoffroy on 08/04/2016.
 */
@RestController
@RequestMapping(value = "/api/bankAccounts")
public class BankAccountController {

    private final BankAccountRepository bankAccountRepository;

    private final AuthenticationFacade authenticationFacade;

    @Autowired
    public BankAccountController(BankAccountRepository bankAccountRepository,
                                 AuthenticationFacade authenticationFacade) {
        this.bankAccountRepository = bankAccountRepository;
        this.authenticationFacade = authenticationFacade;
    }


    @RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json")
    @JsonView(View.Summary.class)
    public @ResponseBody List<BankAccount> index() {
        return bankAccountRepository.findByOwner(authenticationFacade.getCurrentUser());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    @JsonView(View.Summary.class)
    public @ResponseBody BankAccount get(@PathVariable("id") Long id) throws ResourceNotFoundException {
        BankAccount bankAccount =
                bankAccountRepository.findOneByOwnerAndId(authenticationFacade.getCurrentUser(), id);

        if (bankAccount == null) {
            throw new ResourceNotFoundException("No bank account found");
        }

        return bankAccount;
    }


    @RequestMapping(value = "", method = RequestMethod.POST, produces = "application/json",
                    consumes = "application/json")
    @JsonView(View.Summary.class)
    public @ResponseBody BankAccount create(@RequestBody BankAccount bankAccount) {
        bankAccount.setOwner(authenticationFacade.getCurrentUser());
        bankAccountRepository.save(bankAccount);
        return bankAccount;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = "application/json",
                    consumes = "application/json")
    @JsonView(View.Summary.class)
    public @ResponseBody BankAccount update(@PathVariable("id") Long id,
                                            @RequestBody BankAccount update)
            throws ResourceNotFoundException {
        BankAccount bankAccountToUpdate =
                bankAccountRepository.findOneByOwnerAndId(authenticationFacade.getCurrentUser(), id);
        if (bankAccountToUpdate == null) {
            throw new ResourceNotFoundException("No bank account found.");
        }

        bankAccountToUpdate.setCurrency(update.getCurrency());
        bankAccountToUpdate.setName(update.getName());
        bankAccountRepository.save(bankAccountToUpdate);
        return bankAccountToUpdate;
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
