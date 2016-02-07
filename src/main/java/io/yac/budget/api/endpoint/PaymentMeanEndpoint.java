package io.yac.budget.api.endpoint;

import io.katharsis.queryParams.QueryParams;
import io.katharsis.repository.ResourceRepository;
import io.katharsis.resource.exception.ResourceNotFoundException;
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
    private PaymentMeanRepository repository;

    @Autowired
    private PaymentMeanConverter converter;

    @Override
    public PaymentMeanResource findOne(Long id, QueryParams queryParams) {
        PaymentMean PaymentMean = repository.findOne(id);
        if (PaymentMean == null) {
            throw new ResourceNotFoundException("Bank Account not found");
        }

        return converter.convertToResource(PaymentMean);
    }

    @Override
    public Iterable<PaymentMeanResource> findAll(QueryParams queryParams) {
        return StreamSupport.stream(repository.findAll().spliterator(), false).map(a -> converter.convertToResource(a))
                .collect(Collectors.toList());

    }

    @Override
    public Iterable<PaymentMeanResource> findAll(Iterable<Long> longs, QueryParams queryParams) {
        return StreamSupport.stream(repository.findAll(longs).spliterator(), false)
                .map(a -> converter.convertToResource(a))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public PaymentMeanResource save(PaymentMeanResource resource) {
        PaymentMean entity = converter.convertToEntity(resource);
        return converter.convertToResource(repository.save(entity));
    }
}
