package io.yac.budget.repository;

import io.yac.auth.user.model.User;
import io.yac.budget.domain.RecurringTransaction;
import io.yac.budget.domain.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

/**
 * Created by geoffroy on 17/02/2016.
 */
@Component
public interface RecurringTransactionRepository extends PagingAndSortingRepository<RecurringTransaction, Long> {

    Page<RecurringTransaction> findByActive(boolean isActive, Pageable pageable);

    RecurringTransaction findOneByOwnerAndId(User owner, Long id);

    Iterable<RecurringTransaction> findAllByOwner(User owner);

    @Query("select t from RecurringTransaction t where t.owner = ?1 and t.id IN ?2")
    Iterable<RecurringTransaction> findAllByOwnerAndIds(User owner, Iterable<Long> ids);

}
