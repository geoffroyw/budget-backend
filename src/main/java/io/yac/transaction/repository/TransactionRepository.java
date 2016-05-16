package io.yac.transaction.repository;

import io.yac.auth.user.model.User;
import io.yac.transaction.domain.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by geoffroy on 07/02/2016.
 */
@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    Transaction findOneByOwnerAndId(User owner, Long id);

    Iterable<Transaction> findAllByOwner(User owner);

    @Query("select t from Transaction t where t.owner = ?1 and t.id IN ?2")
    Iterable<Transaction> findAllByOwnerAndIds(User owner, Iterable<Long> ids);
}
