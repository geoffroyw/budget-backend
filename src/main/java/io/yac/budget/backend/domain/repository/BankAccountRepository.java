package io.yac.budget.backend.domain.repository;

import io.yac.budget.backend.domain.entity.BankAccount;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by geoffroy on 23/01/2016.
 */
@RepositoryRestResource(collectionResourceRel = "bank-accounts", path = "bank-accounts")
public interface BankAccountRepository extends PagingAndSortingRepository<BankAccount, Long> {

}
