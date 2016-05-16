package io.yac.core.repository;

import io.yac.auth.user.model.User;
import io.yac.core.domain.BankAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by geoffroy on 07/02/2016.
 */
@Repository
public interface BankAccountRepository extends CrudRepository<BankAccount, Long> {


    List<BankAccount> findByOwner(User owner);

    BankAccount findOneByOwnerAndId(User owner, Long id);
}
