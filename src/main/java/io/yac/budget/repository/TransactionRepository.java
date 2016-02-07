package io.yac.budget.repository;

import io.yac.budget.domain.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by geoffroy on 07/02/2016.
 */
@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
}
