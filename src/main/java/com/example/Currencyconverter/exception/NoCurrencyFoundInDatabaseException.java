package com.example.Currencyconverter.exception;

import java.util.NoSuchElementException;

public class NoCurrencyFoundInDatabaseException extends NoSuchElementException {
    public NoCurrencyFoundInDatabaseException() {
        super("Currency not found!");
    }
}
