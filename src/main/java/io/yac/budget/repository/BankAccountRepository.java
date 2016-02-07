package io.yac.budget.repository;

import io.yac.budget.domain.BankAccount;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by geoffroy on 07/02/2016.
 */
public interface BankAccountRepository extends CrudRepository<BankAccount, Long> {
}
