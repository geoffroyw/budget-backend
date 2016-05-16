package io.yac.core.initializer;

import io.yac.Application;
import io.yac.auth.user.model.User;
import io.yac.api.factory.UserFactory;
import io.yac.core.domain.SupportedCurrency;
import io.yac.core.repository.PaymentMeanRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Created by geoffroy on 20/02/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class AccountInitializerTest {

    @Autowired
    PaymentMeanRepository paymentMeanRepository;

    @Autowired
    AccountInitializer accountInitializer;

    @Autowired
    UserFactory userFactory;


    @Test
    public void initialize_creates_a_payment_mean_for_every_major_currency() {
        User user = userFactory.getOrCreateUser("login");

        int number_of_major_currencies =
                Arrays.asList(SupportedCurrency.values()).stream().filter(SupportedCurrency::isMajor).collect(
                        Collectors.toList()).size();

        accountInitializer.initialize(user);

        assertThat(paymentMeanRepository.findByOwner(user), hasSize(number_of_major_currencies));


    }
}