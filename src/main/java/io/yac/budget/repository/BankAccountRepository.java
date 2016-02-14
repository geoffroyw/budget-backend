package io.yac.budget.repository;

import io.yac.auth.user.model.User;
import io.yac.budget.domain.BankAccount;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by geoffroy on 07/02/2016.
 */
@Repository
public interface BankAccountRepository extends CrudRepository<BankAccount, Long> {


    List<BankAccount> findByOwner(User owner);

    @Query("select a from BankAccount a where a.owner = ?1 and a.id IN ?2")
    List<BankAccount> findByOwnerAndIds(User owner, Iterable<Long> ids);

    BankAccount findOneByOwnerAndId(User owner, Long id);
}
