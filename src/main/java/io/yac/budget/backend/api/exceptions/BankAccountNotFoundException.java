package io.yac.budget.backend.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by geoffroy on 24/01/2016.
 */

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such Bank Account")
public class BankAccountNotFoundException extends RuntimeException {
    public BankAccountNotFoundException(String id) {
        super("No bank account found with id " + id);
    }
}
