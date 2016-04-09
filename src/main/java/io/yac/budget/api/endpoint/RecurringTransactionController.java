package io.yac.budget.api.endpoint;

import io.yac.auth.facade.AuthenticationFacade;
import io.yac.budget.api.converter.impl.RecurringTransactionConverter;
import io.yac.budget.api.resources.RecurringTransactionResource;
import io.yac.budget.recurring.transactions.client.RecurringTransactionRequest;
import io.yac.budget.recurring.transactions.client.exception.ResourceNotFoundException;
import io.yac.budget.recurring.transactions.client.resources.RecurringTransactionResponse;
import io.yac.services.clients.recurringtransaction.RecurringTransactionClient;
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
    RecurringTransactionClient recurringTransactionClient;

    @Autowired
    AuthenticationFacade authenticationFacade;

    @RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<RecurringTransactionResource> index() {

        return StreamSupport
                .stream(recurringTransactionClient.findAllByOwner(authenticationFacade.getCurrentUser()).spliterator(),
                        false)
                .map(recurringTransaction -> recurringTransactionConverter.convertToResource(recurringTransaction)).collect(
                        Collectors.toList());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody RecurringTransactionResource get(@PathVariable("id") Long id)
            throws ResourceNotFoundException {
        RecurringTransactionResponse recurringTransactionResponse =
                recurringTransactionClient.findOneByOwnerAndId(authenticationFacade.getCurrentUser(), id);

        if (recurringTransactionResponse == null) {
            throw new ResourceNotFoundException("No payment mean found");
        }

        return recurringTransactionConverter.convertToResource(recurringTransactionResponse);
    }


    @RequestMapping(value = "", method = RequestMethod.POST, produces = "application/json",
                    consumes = "application/json")
    public @ResponseBody RecurringTransactionResource create(@RequestBody RecurringTransactionResource toBeCreated) {
        RecurringTransactionRequest recurringTransactionRequest =
                recurringTransactionConverter.buildRequest(toBeCreated, authenticationFacade.getCurrentUser().getId());

        RecurringTransactionResponse recurringTransactionResponse =
                recurringTransactionClient.create(recurringTransactionRequest);
        return recurringTransactionConverter.convertToResource(recurringTransactionResponse);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = "application/json",
                    consumes = "application/json")
    public @ResponseBody RecurringTransactionResource update(@PathVariable("id") Long id,
                                                             @RequestBody RecurringTransactionResource toBeUpdated)
            throws ResourceNotFoundException {
        if (recurringTransactionClient.findOneByOwnerAndId(authenticationFacade.getCurrentUser(), id) == null) {
            throw new ResourceNotFoundException("No recurring transaction found.");
        }


        final RecurringTransactionRequest recurringTransactionRequest =
                recurringTransactionConverter.buildRequest(toBeUpdated, authenticationFacade.getCurrentUser().getId());

        final RecurringTransactionResponse recurringTransactionResponse =
                recurringTransactionClient.update(id, recurringTransactionRequest);

        LOG.info(
                "Recurring transaction service response for id " + id + " with request " + recurringTransactionRequest +
                        " is " + recurringTransactionResponse);
        return recurringTransactionConverter.convertToResource(recurringTransactionResponse);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) throws ResourceNotFoundException {
        RecurringTransactionResponse recurringTransactionResponse =
                recurringTransactionClient.findOneByOwnerAndId(authenticationFacade.getCurrentUser(), id);

        if (recurringTransactionResponse == null) {
            throw new ResourceNotFoundException("No payment mean found.");
        }

        recurringTransactionClient.delete(id);

    }

}
