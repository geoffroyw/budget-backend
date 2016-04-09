package io.yac.budget.api.endpoint;

import com.google.common.annotations.VisibleForTesting;
import io.yac.auth.facade.AuthenticationFacade;
import io.yac.budget.api.converter.impl.PaymentMeanConverter;
import io.yac.budget.api.resources.PaymentMeanResource;
import io.yac.budget.domain.PaymentMean;
import io.yac.budget.recurring.transactions.client.exception.ResourceNotFoundException;
import io.yac.budget.repository.PaymentMeanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by geoffroy on 08/04/2016.
 */
@RestController
@RequestMapping(value = "/api/paymentMeans")
public class PaymentMeanController {

    @Autowired
    PaymentMeanConverter paymentMeanConverter;

    @Autowired
    PaymentMeanRepository paymentMeanRepository;

    @Autowired
    AuthenticationFacade authenticationFacade;


    public PaymentMeanController() {
    }

    @VisibleForTesting
    PaymentMeanController(PaymentMeanRepository paymentMeanRepository,
                          AuthenticationFacade authenticationFacade,
                          PaymentMeanConverter paymentMeanConverter) {

        this.paymentMeanRepository = paymentMeanRepository;
        this.authenticationFacade = authenticationFacade;
        this.paymentMeanConverter = paymentMeanConverter;
    }

    @RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<PaymentMeanResource> index() {

        return StreamSupport
                .stream(paymentMeanRepository.findByOwner(authenticationFacade.getCurrentUser()).spliterator(), false)
                .map(bankAccount -> paymentMeanConverter.convertToResource(bankAccount)).collect(
                        Collectors.toList());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody PaymentMeanResource get(@PathVariable("id") Long id) throws ResourceNotFoundException {
        PaymentMean paymentMean =
                paymentMeanRepository.findOneByOwnerAndId(authenticationFacade.getCurrentUser(), id);

        if (paymentMean == null) {
            throw new ResourceNotFoundException("No payment mean found");
        }

        return paymentMeanConverter.convertToResource(paymentMean);
    }


    @RequestMapping(value = "", method = RequestMethod.POST, produces = "application/json",
                    consumes = "application/json")
    public @ResponseBody PaymentMeanResource create(@RequestBody PaymentMeanResource toBeCreated) {
        PaymentMean paymentMean = paymentMeanConverter.convertToEntity(toBeCreated, null);
        paymentMean.setOwner(authenticationFacade.getCurrentUser());
        paymentMeanRepository.save(paymentMean);
        return paymentMeanConverter.convertToResource(paymentMean);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = "application/json",
                    consumes = "application/json")
    public @ResponseBody PaymentMeanResource updated(@PathVariable("id") Long id, @RequestBody PaymentMeanResource toBeUpdated)
            throws ResourceNotFoundException {
        if (paymentMeanRepository.findOneByOwnerAndId(authenticationFacade.getCurrentUser(), id) == null) {
            throw new ResourceNotFoundException("No payment mean found.");
        }

        PaymentMean paymentMean = paymentMeanConverter.convertToEntity(toBeUpdated, id);
        paymentMean.setOwner(authenticationFacade.getCurrentUser());
        paymentMeanRepository.save(paymentMean);
        return paymentMeanConverter.convertToResource(paymentMean);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) throws ResourceNotFoundException {
        PaymentMean paymentMean =
                paymentMeanRepository.findOneByOwnerAndId(authenticationFacade.getCurrentUser(), id);

        if (paymentMean == null) {
            throw new ResourceNotFoundException("No payment mean found.");
        }

        paymentMeanRepository.delete(paymentMean);

    }

}
