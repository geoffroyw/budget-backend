package io.yac.paymentmean.repository;

import io.yac.auth.user.model.User;
import io.yac.paymentmean.domain.PaymentMean;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by geoffroy on 07/02/2016.
 */
@Repository
public interface PaymentMeanRepository extends CrudRepository<PaymentMean, Long> {

    List<PaymentMean> findByOwner(User owner);

    @Query("select a from PaymentMean a where a.owner = ?1 and a.id IN ?2")
    List<PaymentMean> findByOwnerAndId(User owner, Iterable<Long> ids);

    PaymentMean findOneByOwnerAndId(User owner, Long id);
}
