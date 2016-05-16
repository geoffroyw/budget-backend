package io.yac.transaction.repository;

import io.yac.auth.user.model.User;
import io.yac.transaction.domain.RecurringTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

/**
 * Created by geoffroy on 16/05/2016.
 */
@Component
public interface RecurringTransactionRepository extends PagingAndSortingRepository<RecurringTransaction, Long> {

    Page<RecurringTransaction> findByActive(boolean isActive, Pageable pageable);

    RecurringTransaction findOneByOwnerAndId(User owner, Long id);

    Iterable<RecurringTransaction> findAllByOwner(User owner);

}
