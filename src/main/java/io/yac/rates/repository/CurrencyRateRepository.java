package io.yac.rates.repository;

import io.yac.rates.domain.CurrencyRate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by geoffroy on 18/02/2016.
 */
@Repository
public interface CurrencyRateRepository extends CrudRepository<CurrencyRate, Long> {

    CurrencyRate findByCurrency(String currency);
}
