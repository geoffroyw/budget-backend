package io.yac.auth.facade;

import io.yac.auth.user.CustomUserDetailsService.CurrentUser;

/**
 * Created by geoffroy on 14/02/2016.
 */
public interface AuthenticationFacade {

    CurrentUser getCurrentUser();
}
