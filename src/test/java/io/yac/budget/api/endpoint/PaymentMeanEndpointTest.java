package io.yac.budget.api.endpoint;

import io.katharsis.resource.exception.ResourceNotFoundException;
import io.yac.Application;
import io.yac.auth.facade.AuthenticationFacade;
import io.yac.auth.user.CustomUserDetailsService;
import io.yac.auth.user.model.User;
import io.yac.budget.api.converter.impl.PaymentMeanConverter;
import io.yac.budget.api.factory.UserFactory;
import io.yac.budget.api.resources.PaymentMeanResource;
import io.yac.budget.domain.PaymentMean;
import io.yac.budget.domain.SupportedCurrency;
import io.yac.budget.repository.PaymentMeanRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Arrays;

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
public class PaymentMeanEndpointTest {

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
        PaymentMeanEndpoint PaymentMeanEndpoint =
                new PaymentMeanEndpoint(paymentMeanRepository, dummy_authentication_facade,
                        paymentMeanConverter);

        assertThat(PaymentMeanEndpoint.findOne(paymentMean.getId(), null).getId(), is(paymentMean.getId()));

    }

    @Test(expected = ResourceNotFoundException.class)
    public void findOne_throws_resource_not_found_exception_if_no_resource_with_the_given_id_exists()
            throws Exception {
        Long unexistingId = -20L;

        User user = userFactory.getOrCreateUser("login1");
        AuthenticationFacade dummy_authentication_facade = mock(AuthenticationFacade.class);
        when(dummy_authentication_facade.getCurrentUser()).thenReturn(new CustomUserDetailsService.CurrentUser(user));
        PaymentMeanEndpoint PaymentMeanEndpoint =
                new PaymentMeanEndpoint(paymentMeanRepository, dummy_authentication_facade,
                        paymentMeanConverter);

        PaymentMeanEndpoint.findOne(unexistingId, null);

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

        PaymentMeanEndpoint PaymentMeanEndpoint =
                new PaymentMeanEndpoint(paymentMeanRepository, dummy_authentication_facade,
                        paymentMeanConverter);

        PaymentMeanEndpoint.findOne(paymentMean_of_user1.getId(), null);

    }

    @Test
    public void save_sets_the_owner() {
        User user = userFactory.getOrCreateUser("login1");

        AuthenticationFacade dummy_authentication_facade = mock(AuthenticationFacade.class);
        when(dummy_authentication_facade.getCurrentUser()).thenReturn(new CustomUserDetailsService.CurrentUser(user));

        PaymentMeanResource resource =
                PaymentMeanResource.builder().currency(SupportedCurrency.AUD.getExternalName()).name("any").build();

        PaymentMeanEndpoint PaymentMeanEndpoint =
                new PaymentMeanEndpoint(paymentMeanRepository, dummy_authentication_facade,
                        paymentMeanConverter);

        Long savedPaymentMeanId = PaymentMeanEndpoint.save(resource).getId();
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
        PaymentMeanEndpoint paymentMeanEndpoint =
                new PaymentMeanEndpoint(paymentMeanRepository, dummy_authentication_facade,
                        paymentMeanConverter);

        Long entityId = paymentMean.getId();
        paymentMeanEndpoint.delete(entityId);
        assertThat(paymentMeanRepository.findOne(entityId), is(nullValue()));

    }

    @Test(expected = ResourceNotFoundException.class)
    public void deletes_throws_resource_not_found_exception_if_no_resource_with_the_given_id_exists()
            throws Exception {
        Long unexistingId = -20L;

        User user = userFactory.getOrCreateUser("login1");
        AuthenticationFacade dummy_authentication_facade = mock(AuthenticationFacade.class);
        when(dummy_authentication_facade.getCurrentUser()).thenReturn(new CustomUserDetailsService.CurrentUser(user));
        PaymentMeanEndpoint PaymentMeanEndpoint =
                new PaymentMeanEndpoint(paymentMeanRepository, dummy_authentication_facade,
                        paymentMeanConverter);

        PaymentMeanEndpoint.delete(unexistingId);

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

        PaymentMeanEndpoint PaymentMeanEndpoint =
                new PaymentMeanEndpoint(paymentMeanRepository, dummy_authentication_facade,
                        paymentMeanConverter);

        Long entityId = paymentMean_of_user_1.getId();
        PaymentMeanEndpoint.delete(entityId);

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

        PaymentMeanEndpoint PaymentMeanEndpoint =
                new PaymentMeanEndpoint(paymentMeanRepository, dummy_authentication_facade,
                        paymentMeanConverter);

        assertThat(PaymentMeanEndpoint.findAll(null),
                is(allOf(
                        hasItem(paymentMeanConverter.convertToResource(paymentMean_of_current_user)),
                        not(hasItem(paymentMeanConverter.convertToResource(paymentMean_of_user_1))))));
    }

    @Test
    public void findAll_ids_returns_all_the_bank_paymentMeans_of_the_current_user()
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

        PaymentMeanEndpoint PaymentMeanEndpoint =
                new PaymentMeanEndpoint(paymentMeanRepository, dummy_authentication_facade,
                        paymentMeanConverter);

        assertThat(
                PaymentMeanEndpoint
                        .findAll(Arrays.asList(paymentMean_of_user_1.getId(), paymentMean_of_current_user.getId()),
                                null),
                is(allOf(
                        hasItem(paymentMeanConverter.convertToResource(paymentMean_of_current_user)),
                        not(hasItem(paymentMeanConverter.convertToResource(paymentMean_of_user_1))))));


    }

}