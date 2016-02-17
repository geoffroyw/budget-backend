package io.yac.budget.repository;

import io.yac.budget.domain.RecurringTransaction;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

/**
 * Created by geoffroy on 17/02/2016.
 */
@Component
public interface RecurringTransactionRepository extends PagingAndSortingRepository<RecurringTransaction, Long> {

}
