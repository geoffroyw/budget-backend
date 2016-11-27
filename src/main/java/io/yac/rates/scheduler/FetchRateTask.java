package io.yac.rates.scheduler;

import io.yac.rates.domain.CurrencyRate;
import io.yac.rates.domain.ExchangeRate;
import io.yac.rates.provider.ecb.ECBRate;
import io.yac.rates.provider.ecb.ECBRateServiceFacade;
import io.yac.rates.repository.CurrencyRateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by geoffroy on 18/02/2016.
 */
@Component
@ManagedResource
public class FetchRateTask {

    private static final Logger LOG = LoggerFactory.getLogger(FetchRateTask.class);

    private final ECBRateServiceFacade ecbRateService;
    private final CurrencyRateRepository rateRepository;

    @Autowired
    public FetchRateTask(ECBRateServiceFacade ecbRateService, CurrencyRateRepository rateRepository) {
        this.ecbRateService = ecbRateService;
        this.rateRepository = rateRepository;
    }

    @Scheduled(cron = "30 14 * * * *") //Every day at 14:30
    @ManagedOperation(description = "Trigger the fetch ECB rate task.")
    @Transactional
    public void fetchRate() {
        List<ECBRate> ecbRates = ecbRateService.fetchRates();
        LOG.info("Rates from ECB:" + ecbRates);

        for (ECBRate ecbRate : ecbRates) {
            CurrencyRate currencyRate = rateRepository.findByCurrency(ecbRate.getCurrency());
            if (currencyRate == null) {
                currencyRate = new CurrencyRate();
                currencyRate.setCurrency(ecbRate.getCurrency());
                currencyRate.setRates(new ArrayList<>());
            }
            currencyRate.getRates()
                    .add(ExchangeRate.builder().currencyRate(currencyRate).amount(ecbRate.getRate())
                            .date(ecbRate.getDate()).build());
            currencyRate.setLastUpdatedOn(new Date());

            rateRepository.save(currencyRate);
        }

    }
}
