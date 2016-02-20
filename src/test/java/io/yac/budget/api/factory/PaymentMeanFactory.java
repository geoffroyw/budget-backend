package io.yac.budget.api.factory;

import io.yac.auth.user.model.User;
import io.yac.budget.domain.PaymentMean;
import io.yac.budget.domain.SupportedCurrency;
import io.yac.budget.repository.PaymentMeanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by geoffroy on 14/02/2016.
 */
@Component
public class PaymentMeanFactory {

    @Autowired
    PaymentMeanRepository paymentMeanRepository;

    public PaymentMean savePaymentMean(User owner) {
        return paymentMeanRepository
                .save(PaymentMean.builder().currency(SupportedCurrency.EUR).name("any").owner(owner).build());
    }
}
