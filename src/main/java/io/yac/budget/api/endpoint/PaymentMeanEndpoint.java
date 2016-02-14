package io.yac.budget.api.endpoint;

import com.google.common.annotations.VisibleForTesting;
import io.katharsis.queryParams.QueryParams;
import io.katharsis.repository.ResourceRepository;
import io.katharsis.resource.exception.ResourceNotFoundException;
import io.yac.auth.facade.AuthenticationFacade;
import io.yac.budget.api.converter.impl.PaymentMeanConverter;
import io.yac.budget.api.resources.PaymentMeanResource;
import io.yac.budget.domain.PaymentMean;
import io.yac.budget.repository.PaymentMeanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by geoffroy on 07/02/2016.
 */
@Component
public class PaymentMeanEndpoint implements ResourceRepository<PaymentMeanResource, Long> {

    @Autowired
    AuthenticationFacade authenticationFacade;
    @Autowired
    private PaymentMeanRepository repository;
    @Autowired
    private PaymentMeanConverter converter;

    public PaymentMeanEndpoint() {
    }

    @VisibleForTesting
    public PaymentMeanEndpoint(PaymentMeanRepository paymentMeanRepository,
                               AuthenticationFacade authenticationFacade,
                               PaymentMeanConverter paymentMeanConverter) {

        this.repository = paymentMeanRepository;
        this.authenticationFacade = authenticationFacade;
        this.converter = paymentMeanConverter;
    }

    @Override
    public PaymentMeanResource findOne(Long id, QueryParams queryParams) {
        PaymentMean paymentMean = repository.findOneByOwnerAndId(authenticationFacade.getCurrentUser(), id);
        if (paymentMean == null) {
            throw new ResourceNotFoundException("Payment Mean not found");
        }

        return converter.convertToResource(paymentMean);
    }

    @Override
    public Iterable<PaymentMeanResource> findAll(QueryParams queryParams) {
        return StreamSupport.stream(repository.findByOwner(authenticationFacade.getCurrentUser()).spliterator(), false)
                .map(a -> converter.convertToResource(a))
                .collect(Collectors.toList());

    }

    @Override
    public Iterable<PaymentMeanResource> findAll(Iterable<Long> ids, QueryParams queryParams) {
        return StreamSupport
                .stream(repository.findByOwnerAndId(authenticationFacade.getCurrentUser(), ids).spliterator(), false)
                .map(a -> converter.convertToResource(a))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        PaymentMean paymentMean = repository.findOneByOwnerAndId(authenticationFacade.getCurrentUser(), id);
        if (paymentMean == null) {
            throw new ResourceNotFoundException("Payment Mean not found");
        }
        repository.delete(id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public PaymentMeanResource save(PaymentMeanResource resource) {
        PaymentMean entity = converter.convertToEntity(resource);
        entity.setOwner(authenticationFacade.getCurrentUser());
        return converter.convertToResource(repository.save(entity));
    }
}
