package io.yac.auth.api.endpoint;

import io.yac.auth.api.resource.UserResource;
import io.yac.auth.user.UserRepository;
import io.yac.auth.user.model.User;
import io.yac.bankaccount.initializer.AccountInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by geoffroy on 20/02/2016.
 */
@RestController
@RequestMapping("/auth")
public class UserEndpoint {

    private static final Logger LOG = LoggerFactory.getLogger(UserEndpoint.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    AccountInitializer accountInitializer;

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public UserResource create(@RequestBody UserResource resource) {
        LOG.info("Creating new user");

        User user = User.builder().password(new String(resource.getPassword())).email(resource.getEmail())
                .login(resource.getLogin()).build();
        userRepository.save(user);

        accountInitializer.initialize(user);

        return new UserResource(user);
    }
}
