package io.yac.budget.api.endpoint;

import com.google.common.annotations.VisibleForTesting;
import io.yac.auth.facade.AuthenticationFacade;
import io.yac.budget.api.converter.impl.TransactionConverter;
import io.yac.budget.api.resources.TransactionResource;
import io.yac.budget.domain.Transaction;
import io.yac.budget.recurring.transactions.client.exception.ResourceNotFoundException;
import io.yac.budget.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by geoffroy on 08/04/2016.
 */
@RestController
@RequestMapping(value = "/api/transactions")
public class TransactionController {

    @Autowired
    TransactionConverter transactionConverter;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    AuthenticationFacade authenticationFacade;

    public TransactionController() {
    }

    @VisibleForTesting
    TransactionController(TransactionRepository transactionRepository,
                          AuthenticationFacade authenticationFacade,
                          TransactionConverter transactionConverter) {

        this.transactionRepository = transactionRepository;
        this.authenticationFacade = authenticationFacade;
        this.transactionConverter = transactionConverter;
    }

    @RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<TransactionResource> index() {

        return StreamSupport
                .stream(transactionRepository.findAllByOwner(authenticationFacade.getCurrentUser()).spliterator(),
                        false)
                .map(bankAccount -> transactionConverter.convertToResource(bankAccount)).collect(
                        Collectors.toList());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody TransactionResource get(@PathVariable("id") Long id) throws ResourceNotFoundException {
        Transaction transaction =
                transactionRepository.findOneByOwnerAndId(authenticationFacade.getCurrentUser(), id);

        if (transaction == null) {
            throw new ResourceNotFoundException("No transaction found");
        }

        return transactionConverter.convertToResource(transaction);
    }


    @RequestMapping(value = "", method = RequestMethod.POST, produces = "application/json",
                    consumes = "application/json")
    public @ResponseBody TransactionResource create(@RequestBody TransactionResource toBeCreated) {
        Transaction transaction = transactionConverter.convertToEntity(toBeCreated);
        transaction.setOwner(authenticationFacade.getCurrentUser());
        transactionRepository.save(transaction);
        return transactionConverter.convertToResource(transaction);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = "application/json",
                    consumes = "application/json")
    public @ResponseBody TransactionResource updated(@PathVariable("id") Long id, @RequestBody TransactionResource toBeUpdated)
            throws ResourceNotFoundException {
        if (transactionRepository.findOneByOwnerAndId(authenticationFacade.getCurrentUser(), id) == null) {
            throw new ResourceNotFoundException("No transaction found.");
        }

        if (!toBeUpdated.getId().equals(id)) {
            throw new ResourceNotFoundException("No transaction found.");
        }

        Transaction transaction = transactionConverter.convertToEntity(toBeUpdated);
        transaction.setOwner(authenticationFacade.getCurrentUser());
        transactionRepository.save(transaction);
        return transactionConverter.convertToResource(transaction);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) throws ResourceNotFoundException {
        Transaction transaction =
                transactionRepository.findOneByOwnerAndId(authenticationFacade.getCurrentUser(), id);

        if (transaction == null) {
            throw new ResourceNotFoundException("No transaction found.");
        }

        transactionRepository.delete(transaction);

    }

}
