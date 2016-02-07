package io.yac.budget.repository;

import io.yac.budget.domain.Transaction;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by geoffroy on 07/02/2016.
 */
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
}
