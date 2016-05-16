package io.yac.transaction.api.endpoint;

import io.yac.transaction.api.converter.RecurringTransactionConverter;
import io.yac.api.exceptions.ResourceNotFoundException;
import io.yac.transaction.api.RecurringTransactionResource;
import io.yac.auth.facade.AuthenticationFacade;
import io.yac.transaction.domain.RecurringTransaction;
import io.yac.transaction.repository.RecurringTransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by geoffroy on 08/04/2016.
 */
@RestController
@RequestMapping(value = "/api/recurringTransactions")
public class RecurringTransactionController {

    private static final Logger LOG = LoggerFactory.getLogger(RecurringTransactionController.class);

    @Autowired
    RecurringTransactionConverter recurringTransactionConverter;

    @Autowired
    RecurringTransactionRepository recurringTransactionRepository;

    @Autowired
    AuthenticationFacade authenticationFacade;

    @RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<RecurringTransactionResource> index() {

        return StreamSupport
                .stream(recurringTransactionRepository.findAllByOwner(authenticationFacade.getCurrentUser())
                                .spliterator(),
                        false)
                .map(recurringTransaction -> recurringTransactionConverter.convertToResource(recurringTransaction))
                .collect(
                        Collectors.toList());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody RecurringTransactionResource get(@PathVariable("id") Long id)
            throws ResourceNotFoundException {
        RecurringTransaction recurringTransactionResponse =
                recurringTransactionRepository.findOneByOwnerAndId(authenticationFacade.getCurrentUser(), id);

        if (recurringTransactionResponse == null) {
            throw new ResourceNotFoundException("No payment mean found");
        }

        return recurringTransactionConverter.convertToResource(recurringTransactionResponse);
    }


    @RequestMapping(value = "", method = RequestMethod.POST, produces = "application/json",
                    consumes = "application/json")
    public @ResponseBody RecurringTransactionResource create(@RequestBody RecurringTransactionResource toBeCreated) {
        RecurringTransaction recurringTransaction =
                recurringTransactionRepository.save(recurringTransactionConverter.convertToEntity(toBeCreated, null));

        return recurringTransactionConverter.convertToResource(recurringTransaction);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = "application/json",
                    consumes = "application/json")
    public @ResponseBody RecurringTransactionResource update(@PathVariable("id") Long id,
                                                             @RequestBody RecurringTransactionResource toBeUpdated)
            throws ResourceNotFoundException {
        if (recurringTransactionRepository.findOneByOwnerAndId(authenticationFacade.getCurrentUser(), id) == null) {
            throw new ResourceNotFoundException("No recurring transaction found.");
        }


        final RecurringTransaction recurringTransaction =
                recurringTransactionConverter
                        .convertToEntity(toBeUpdated, id);

        return recurringTransactionConverter
                .convertToResource(recurringTransactionRepository.save(recurringTransaction));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) throws ResourceNotFoundException {
        RecurringTransaction recurringTransaction =
                recurringTransactionRepository.findOneByOwnerAndId(authenticationFacade.getCurrentUser(), id);

        if (recurringTransaction == null) {
            throw new ResourceNotFoundException("No payment mean found.");
        }

        recurringTransactionRepository.delete(id);

    }

}
