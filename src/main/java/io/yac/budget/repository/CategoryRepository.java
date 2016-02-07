package io.yac.budget.repository;

import io.yac.budget.domain.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by geoffroy on 07/02/2016.
 */
@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {
}
