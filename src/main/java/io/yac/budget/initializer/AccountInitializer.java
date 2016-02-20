package io.yac.budget.initializer;

import io.yac.auth.user.model.User;
import io.yac.budget.domain.PaymentMean;
import io.yac.budget.domain.SupportedCurrency;
import io.yac.budget.repository.PaymentMeanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by geoffroy on 20/02/2016.
 */
@Service
public class AccountInitializer {

    @Autowired
    PaymentMeanRepository paymentMeanRepository;

    public void initialize(User user) {
        initializePaymentMeans(user);
    }

    private void initializePaymentMeans(User user) {
        List<SupportedCurrency> supportedCurrencies =
                Arrays.asList(SupportedCurrency.values()).stream().filter(SupportedCurrency::isMajor).collect(
                        Collectors.toList());

        paymentMeanRepository.save(supportedCurrencies.stream().map((c) -> PaymentMean.builder().owner(user).currency(c)
                .name("Bank Transfer (" + c.getExternalName() + ")").build()).collect(Collectors.toList()));

    }

}
