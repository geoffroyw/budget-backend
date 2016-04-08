package io.yac.services.clients.recurringtransaction;

import io.yac.auth.user.CustomUserDetailsService;
import io.yac.budget.recurring.transactions.client.RecurringTransactionRequest;
import io.yac.budget.recurring.transactions.client.exception.ResourceNotFoundException;
import io.yac.budget.recurring.transactions.client.resources.RecurringTransactionResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by geoffroy on 03/04/2016.
 */
@FeignClient(value = "recurring-transactions")
public interface RecurringTransactionClient {

    @RequestMapping(value = "/api/{id}", method = RequestMethod.GET, consumes = "application/json")
    RecurringTransactionResponse getById(@PathVariable("id") Long id) throws ResourceNotFoundException;

    @RequestMapping(value = "/api/", method = RequestMethod.GET, consumes = "application/json")
    List<RecurringTransactionResponse> findAll();


    @RequestMapping(value = "/api", method = RequestMethod.POST, consumes = "application/json")
    RecurringTransactionResponse create(@RequestBody RecurringTransactionRequest request);

    @RequestMapping(value = "/api/{id}", method = RequestMethod.POST, consumes = "application/json")
    RecurringTransactionResponse update(@PathVariable("id") Long id, @RequestBody RecurringTransactionRequest request);


    @RequestMapping(value = "/api/{id}", method = RequestMethod.DELETE, consumes = "application/json")
    void delete(@PathVariable("id") Long id);


    default RecurringTransactionResponse findOneByOwnerAndId(CustomUserDetailsService.CurrentUser currentUser, Long id)
            throws ResourceNotFoundException {
        RecurringTransactionResponse response = getById(id);
        if (response == null || !response.getOwnerId().equals(currentUser.getId())) {
            throw new ResourceNotFoundException("Recurring transaction not found");
        }
        return response;

    }

    default List<RecurringTransactionResponse> findAllByOwner(CustomUserDetailsService.CurrentUser currentUser) {
        List<RecurringTransactionResponse> recurringTransactionResponses = findAll();
        return recurringTransactionResponses.stream()
                .filter((transaction) -> transaction.getOwnerId().equals(currentUser.getId())).collect(
                        Collectors.toList());
    }
}
