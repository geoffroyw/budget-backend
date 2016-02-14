package io.yac.budget.api.factory;

import io.yac.auth.user.UserRepository;
import io.yac.auth.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by geoffroy on 14/02/2016.
 */
@Component
public class UserFactory {

    @Autowired
    UserRepository userRepository;

    public User getOrCreateUser(String login) {
        User user = userRepository.findByLogin(login);
        if (user == null) {
            user = userRepository
                    .save(User.builder().login(login).password("password").firstName("firstName").lastName("lastName")
                            .build());
        }
        return user;
    }

}
