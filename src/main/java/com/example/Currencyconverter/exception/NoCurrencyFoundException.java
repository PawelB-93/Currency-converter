package com.example.Currencyconverter.exception;

import java.util.NoSuchElementException;

public class NoCurrencyFoundException extends NoSuchElementException {
    public NoCurrencyFoundException() {
        super("Currency not found!");
    }
}
