package io.yac.budget.repository;

import io.yac.budget.domain.BankAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by geoffroy on 07/02/2016.
 */
@Repository
public interface BankAccountRepository extends CrudRepository<BankAccount, Long> {
}
