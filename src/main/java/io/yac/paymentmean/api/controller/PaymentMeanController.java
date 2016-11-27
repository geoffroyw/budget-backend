package io.yac.paymentmean.api.controller;

import com.fasterxml.jackson.annotation.JsonView;
import io.yac.auth.facade.AuthenticationFacade;
import io.yac.common.api.View;
import io.yac.common.api.exceptions.ResourceNotFoundException;
import io.yac.paymentmean.domain.PaymentMean;
import io.yac.paymentmean.repository.PaymentMeanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by geoffroy on 08/04/2016.
 */
@RestController
@RequestMapping(value = "/api/paymentMeans")
public class PaymentMeanController {

    private final PaymentMeanRepository paymentMeanRepository;

    private final AuthenticationFacade authenticationFacade;

    @Autowired
    public PaymentMeanController(PaymentMeanRepository paymentMeanRepository,
                                 AuthenticationFacade authenticationFacade) {
        this.paymentMeanRepository = paymentMeanRepository;
        this.authenticationFacade = authenticationFacade;
    }

    @RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json")
    @JsonView(View.Default.class)
    public @ResponseBody List<PaymentMean> index() {
        return paymentMeanRepository.findByOwner(authenticationFacade.getCurrentUser());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    @JsonView(View.Default.class)
    public @ResponseBody PaymentMean get(@PathVariable("id") Long id) throws ResourceNotFoundException {
        PaymentMean paymentMean =
                paymentMeanRepository.findOneByOwnerAndId(authenticationFacade.getCurrentUser(), id);

        if (paymentMean == null) {
            throw new ResourceNotFoundException("No payment mean found");
        }

        return paymentMean;
    }


    @RequestMapping(value = "", method = RequestMethod.POST, produces = "application/json",
                    consumes = "application/json")
    @JsonView(View.Default.class)
    public @ResponseBody PaymentMean create(@RequestBody PaymentMean paymentMean) {
        paymentMean.setOwner(authenticationFacade.getCurrentUser());
        paymentMeanRepository.save(paymentMean);
        return paymentMean;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = "application/json",
                    consumes = "application/json")
    @JsonView(View.Default.class)
    public @ResponseBody PaymentMean updated(@PathVariable("id") Long id,
                                             @RequestBody PaymentMean toBeUpdated)
            throws ResourceNotFoundException {
        PaymentMean paymentMean =
                paymentMeanRepository.findOneByOwnerAndId(authenticationFacade.getCurrentUser(), id);
        if (paymentMean == null) {
            throw new ResourceNotFoundException("No payment mean found.");
        }

        paymentMean.setName(toBeUpdated.getName());
        paymentMean.setCurrency(toBeUpdated.getCurrency());
        paymentMeanRepository.save(paymentMean);
        return paymentMean;
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
