package io.yac.auth.facade.impl;

import io.yac.auth.facade.AuthenticationFacade;
import io.yac.auth.user.CustomUserDetailsService;
import io.yac.auth.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Created by geoffroy on 14/02/2016.
 */
@Component
public class AuthenticationFacadeImpl implements AuthenticationFacade {

    @Autowired
    UserRepository userRepository;

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public CustomUserDetailsService.CurrentUser getCurrentUser() {
        return (CustomUserDetailsService.CurrentUser) getAuthentication().getPrincipal();
    }
}
