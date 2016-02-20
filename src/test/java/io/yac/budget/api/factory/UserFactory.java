package io.yac.budget.api.factory;

import io.yac.auth.user.UserRepository;
import io.yac.auth.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by geoffroy on 14/02/2016.
 */
@Component
public class UserFactory {

    private static final AtomicInteger counter = new AtomicInteger(1);
    @Autowired
    UserRepository userRepository;

    public User getOrCreateUser(String login) {
        User user = userRepository.findByLogin(login);
        if (user == null) {
            user = userRepository
                    .save(User.builder().login(login).password("password")
                            .email(login + counter.getAndIncrement() + "@bar.com").build());
        }
        return user;
    }

}
