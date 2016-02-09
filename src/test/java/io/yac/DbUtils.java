package io.yac;

import io.yac.auth.user.UserRepository;
import io.yac.auth.user.model.Role;
import io.yac.auth.user.model.User;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;

/**
 * Created by geoffroy on 09/02/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class DbUtils {

    @Autowired
    UserRepository userRepository;


    @Test
    @Ignore
    public void createUser() {
        Role userRole = new Role();
        userRole.setName(Role.RoleName.USER);


        User user = new User();
        user.setFirstName("TestFirstName");
        user.setLastName("TestLastName");
        user.setLogin("test");
        user.setPassword(new BCryptPasswordEncoder().encode("test"));
        user.setRoles(Collections.singleton(userRole));

        userRepository.save(user);
    }
}
