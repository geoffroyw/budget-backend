package io.yac.api.factory;

import io.yac.auth.facade.AuthenticationFacade;
import io.yac.auth.user.CustomUserDetailsService;
import io.yac.auth.user.model.User;

/**
 * Created by geoffroy on 14/02/2016.
 */


public class DummyAuthenticationFacade implements AuthenticationFacade {
    private User currentUser;

    public DummyAuthenticationFacade(User currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public CustomUserDetailsService.CurrentUser getCurrentUser() {
        return new CustomUserDetailsService.CurrentUser(currentUser);
    }
}
