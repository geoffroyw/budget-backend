package io.yac.categories.repository;

import io.yac.auth.user.model.User;
import io.yac.categories.domain.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by geoffroy on 07/02/2016.
 */
@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {
    List<Category> findByOwner(User owner);

    Category findOneByOwnerAndId(User owner, Long id);
}
