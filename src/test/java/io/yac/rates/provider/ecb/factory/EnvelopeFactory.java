package io.yac.rates.provider.ecb.factory;


import io.yac.rates.provider.ecb.iface.Cube;
import io.yac.rates.provider.ecb.iface.Envelope;
import io.yac.rates.provider.ecb.iface.EnvelopeObjectFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by geoffroy on 18/02/2016.
 */
public class EnvelopeFactory {
    private EnvelopeObjectFactory objectFactory = new EnvelopeObjectFactory();
    private List<Rate> rates = new ArrayList<>();
    private String date;

    public static EnvelopeFactory factory() {
        return new EnvelopeFactory();
    }

    public EnvelopeFactory date(String yyyy_dash_MM_dash_dd) {
        date = yyyy_dash_MM_dash_dd;
        return this;
    }

    public EnvelopeFactory addRate(Rate rate) {
        rates.add(rate);
        return this;
    }


    public Envelope build() {
        Envelope envelope = objectFactory.createEnvelope();
        Cube rootCube = objectFactory.createCube();
        envelope.setCube(rootCube);

        Cube dateCube = objectFactory.createCube();
        dateCube.setTime(date);
        rootCube.getCube().add(dateCube);

        for (Rate rate : rates) {
            Cube rateCube = objectFactory.createCube();
            rateCube.setCurrency(rate.currency);
            rateCube.setRate(rate.rate);
            dateCube.getCube().add(rateCube);
        }

        return envelope;
    }


    public static class Rate {

        private String currency;
        private BigDecimal rate;

        private Rate(String currency, BigDecimal rate) {

            this.currency = currency;
            this.rate = rate;
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private String currency;
            private BigDecimal rate;

            public Builder currency(String currency) {
                this.currency = currency;
                return this;
            }

            public Builder rate(BigDecimal rate) {
                this.rate = rate;
                return this;
            }

            public Rate build() {
                return new Rate(currency, rate);
            }
        }


    }
}
