package io.yac.budget.backend.domain.repository;

import io.yac.budget.backend.domain.entity.BankAccount;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by geoffroy on 23/01/2016.
 */
public interface BankAccountRepository extends CrudRepository<BankAccount, Long> {

}
