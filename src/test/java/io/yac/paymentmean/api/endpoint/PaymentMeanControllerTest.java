package io.yac.paymentmean.api.endpoint;

import io.yac.Application;
import io.yac.paymentmean.api.converter.PaymentMeanConverter;
import io.yac.common.api.exceptions.ResourceNotFoundException;
import io.yac.api.factory.UserFactory;
import io.yac.paymentmean.api.PaymentMeanResource;
import io.yac.auth.facade.AuthenticationFacade;
import io.yac.auth.user.CustomUserDetailsService;
import io.yac.auth.user.model.User;
import io.yac.paymentmean.api.endpoint.PaymentMeanController;
import io.yac.paymentmean.domain.PaymentMean;
import io.yac.common.domain.SupportedCurrency;
import io.yac.paymentmean.repository.PaymentMeanRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by geoffroy on 14/02/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(classes = Application.class)
public class PaymentMeanControllerTest {

    @Autowired
    PaymentMeanConverter paymentMeanConverter;

    @Autowired
    PaymentMeanRepository paymentMeanRepository;

    @Autowired
    UserFactory userFactory;


    @Test
    public void findOne_returns_the_resource_with_the_given_id() throws Exception {
        User user = userFactory.getOrCreateUser("login1");
        PaymentMean paymentMean =
                PaymentMean.builder().currency(SupportedCurrency.EUR).name("any").owner(user).build();
        paymentMeanRepository.save(paymentMean);


        AuthenticationFacade dummy_authentication_facade = mock(AuthenticationFacade.class);
        when(dummy_authentication_facade.getCurrentUser()).thenReturn(new CustomUserDetailsService.CurrentUser(user));
        PaymentMeanController paymentMeanController =
                new PaymentMeanController(paymentMeanRepository, dummy_authentication_facade,
                        paymentMeanConverter);

        assertThat(paymentMeanController.get(paymentMean.getId()).getId(), is(paymentMean.getId()));

    }

    @Test(expected = ResourceNotFoundException.class)
    public void findOne_throws_resource_not_found_exception_if_no_resource_with_the_given_id_exists()
            throws Exception {
        Long unexistingId = -20L;

        User user = userFactory.getOrCreateUser("login1");
        AuthenticationFacade dummy_authentication_facade = mock(AuthenticationFacade.class);
        when(dummy_authentication_facade.getCurrentUser()).thenReturn(new CustomUserDetailsService.CurrentUser(user));
        PaymentMeanController paymentMeanController =
                new PaymentMeanController(paymentMeanRepository, dummy_authentication_facade,
                        paymentMeanConverter);

        paymentMeanController.get(unexistingId);

    }

    @Test(expected = ResourceNotFoundException.class)
    public void findOne_throws_resource_not_found_exception_if_resource_with_the_given_id_exists_but_belongs_to_another_user()
            throws Exception {
        User user1 = userFactory.getOrCreateUser("login1");
        User user2 = userFactory.getOrCreateUser("login2");

        PaymentMean paymentMean_of_user1 =
                PaymentMean.builder().currency(SupportedCurrency.EUR).name("any").owner(user1).build();
        paymentMeanRepository.save(paymentMean_of_user1);

        AuthenticationFacade dummy_authentication_facade = mock(AuthenticationFacade.class);
        when(dummy_authentication_facade.getCurrentUser()).thenReturn(new CustomUserDetailsService.CurrentUser(user2));

        PaymentMeanController paymentMeanController =
                new PaymentMeanController(paymentMeanRepository, dummy_authentication_facade,
                        paymentMeanConverter);

        paymentMeanController.get(paymentMean_of_user1.getId());

    }

    @Test
    public void save_sets_the_owner() {
        User user = userFactory.getOrCreateUser("login1");

        AuthenticationFacade dummy_authentication_facade = mock(AuthenticationFacade.class);
        when(dummy_authentication_facade.getCurrentUser()).thenReturn(new CustomUserDetailsService.CurrentUser(user));

        PaymentMeanResource resource =
                PaymentMeanResource.builder().currency(SupportedCurrency.AUD.getExternalName()).name("any").build();

        PaymentMeanController paymentMeanController =
                new PaymentMeanController(paymentMeanRepository, dummy_authentication_facade,
                        paymentMeanConverter);

        Long savedPaymentMeanId = paymentMeanController.create(resource).getId();
        assertThat(paymentMeanRepository.findOne(savedPaymentMeanId).getOwner().getId(), is(user.getId()));


    }


    @Test
    public void delete_deletes_the_resource_with_the_given_id() throws Exception {
        User user = userFactory.getOrCreateUser("login1");
        PaymentMean paymentMean =
                PaymentMean.builder().currency(SupportedCurrency.EUR).name("any").owner(user).build();
        paymentMeanRepository.save(paymentMean);


        AuthenticationFacade dummy_authentication_facade = mock(AuthenticationFacade.class);
        when(dummy_authentication_facade.getCurrentUser()).thenReturn(new CustomUserDetailsService.CurrentUser(user));
        PaymentMeanController paymentMeanController =
                new PaymentMeanController(paymentMeanRepository, dummy_authentication_facade,
                        paymentMeanConverter);

        Long entityId = paymentMean.getId();
        paymentMeanController.delete(entityId);
        assertThat(paymentMeanRepository.findOne(entityId), is(nullValue()));

    }

    @Test(expected = ResourceNotFoundException.class)
    public void deletes_throws_resource_not_found_exception_if_no_resource_with_the_given_id_exists()
            throws Exception {
        Long unexistingId = -20L;

        User user = userFactory.getOrCreateUser("login1");
        AuthenticationFacade dummy_authentication_facade = mock(AuthenticationFacade.class);
        when(dummy_authentication_facade.getCurrentUser()).thenReturn(new CustomUserDetailsService.CurrentUser(user));
        PaymentMeanController paymentMeanController =
                new PaymentMeanController(paymentMeanRepository, dummy_authentication_facade,
                        paymentMeanConverter);

        paymentMeanController.delete(unexistingId);

    }

    @Test(expected = ResourceNotFoundException.class)
    public void deletes_throws_resource_not_found_exception_if_resource_belongs_to_another_user()
            throws Exception {
        User user1 = userFactory.getOrCreateUser("login1");
        User user2 = userFactory.getOrCreateUser("login2");

        PaymentMean paymentMean_of_user_1 =
                PaymentMean.builder().currency(SupportedCurrency.EUR).name("any").owner(user1).build();
        paymentMeanRepository.save(paymentMean_of_user_1);

        AuthenticationFacade dummy_authentication_facade = mock(AuthenticationFacade.class);
        when(dummy_authentication_facade.getCurrentUser()).thenReturn(new CustomUserDetailsService.CurrentUser(user2));

        PaymentMeanController paymentMeanController =
                new PaymentMeanController(paymentMeanRepository, dummy_authentication_facade,
                        paymentMeanConverter);

        Long entityId = paymentMean_of_user_1.getId();
        paymentMeanController.delete(entityId);

    }


    @Test
    public void findAll_returns_all_the_bank_paymentMeans_of_the_current_user()
            throws Exception {
        User user1 = userFactory.getOrCreateUser("login1");
        User currentUser = userFactory.getOrCreateUser("login2");

        PaymentMean paymentMean_of_user_1 =
                PaymentMean.builder().currency(SupportedCurrency.EUR).name("any").owner(user1).build();
        PaymentMean paymentMean_of_current_user =
                PaymentMean.builder().currency(SupportedCurrency.EUR).name("any").owner(currentUser).build();
        paymentMeanRepository.save(paymentMean_of_user_1);
        paymentMeanRepository.save(paymentMean_of_current_user);

        AuthenticationFacade dummy_authentication_facade = mock(AuthenticationFacade.class);
        when(dummy_authentication_facade.getCurrentUser())
                .thenReturn(new CustomUserDetailsService.CurrentUser(currentUser));

        PaymentMeanController paymentMeanController =
                new PaymentMeanController(paymentMeanRepository, dummy_authentication_facade,
                        paymentMeanConverter);

        assertThat(paymentMeanController.index(),
                is(allOf(
                        hasItem(paymentMeanConverter.convertToResource(paymentMean_of_current_user)),
                        not(hasItem(paymentMeanConverter.convertToResource(paymentMean_of_user_1))))));
    }


}