package io.yac.auth.user;

import io.yac.auth.user.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by geoffroy on 08/02/2016.
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findByLogin(String login);
}
