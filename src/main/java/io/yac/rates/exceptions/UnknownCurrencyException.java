package io.yac.rates.exceptions;

/**
 * Created by geoffroy on 18/02/2016.
 */
public class UnknownCurrencyException extends Exception {

    public UnknownCurrencyException() {
        super();
    }

    public UnknownCurrencyException(String message) {
        super(message);
    }
}
