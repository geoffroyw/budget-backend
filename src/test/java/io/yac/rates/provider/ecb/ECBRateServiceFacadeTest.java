package io.yac.rates.provider.ecb;

import io.yac.rates.provider.ecb.factory.EnvelopeFactory;
import io.yac.rates.provider.ecb.factory.EnvelopeFactory.Rate;
import io.yac.rates.provider.ecb.iface.Envelope;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Created by geoffroy on 17/02/2016.œ
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = io.yac.Application.class)
public class ECBRateServiceFacadeTest {

    @Autowired
    ECBRateServiceFacade ecbRateServiceFacade;


    @Test
    public void testConsumeRates() {
        List<ECBRate> ecbRates = ecbRateServiceFacade.fetchRates();
        assertThat(ecbRates, is(notNullValue()));
    }


    @Test
    public void processServiceResponse_returns_a_list_with_as_many_object_as_the_rates_in_ecb_response() {
        String anyCurrencyCode = "USD";
        Envelope envelope =
                EnvelopeFactory.factory().date("2016-02-18").addRate(
                        Rate.builder().currency(anyCurrencyCode).build())
                        .addRate(Rate.builder().currency(anyCurrencyCode).build()).build();

        ECBRateServiceFacade ecbRateServiceFacade = new ECBRateServiceFacade();

        assertThat(ecbRateServiceFacade.processServiceResponse(envelope), hasSize(2));
    }

    @Test
    public void ecbRate_date_is_mapping_to_first_cube_date() {
        Date ecbDate = Date.from(LocalDate.of(2016, 2, 18).atStartOfDay(ZoneId.systemDefault()).toInstant());

        String anyCurrencyCode = "USD";
        Envelope envelope =
                EnvelopeFactory.factory().date("2016-02-18").addRate(Rate.builder().currency(anyCurrencyCode).build())
                        .build();

        ECBRateServiceFacade ecbRateServiceFacade = new ECBRateServiceFacade();

        assertThat(ecbRateServiceFacade.processServiceResponse(envelope).get(0).getDate(), is(ecbDate));
    }

    @Test
    public void ecbRate_currency_is_mapping_to_the_currency() {

        String anyDate = "2016-02-18";
        Envelope envelope =
                EnvelopeFactory.factory().date(anyDate).addRate(Rate.builder().currency("USD").build())
                        .build();

        ECBRateServiceFacade ecbRateServiceFacade = new ECBRateServiceFacade();

        assertThat(ecbRateServiceFacade.processServiceResponse(envelope).get(0).getCurrency(),
                is("USD"));
    }

    @Test
    public void ecbRate_rate_is_mapping_to_the_rate() {
        String anyDate = "2016-02-18";
        String anyCurrencyCode = "USD";
        Envelope envelope =
                EnvelopeFactory.factory().date(anyDate)
                        .addRate(Rate.builder().currency(anyCurrencyCode).rate(BigDecimal.TEN).build()).build();

        ECBRateServiceFacade ecbRateServiceFacade = new ECBRateServiceFacade();

        assertThat(ecbRateServiceFacade.processServiceResponse(envelope).get(0).getRate(), is(BigDecimal.TEN));
    }

}