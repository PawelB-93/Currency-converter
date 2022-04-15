package com.example.Currencyconverter.exception;

public class NoCurrencyFoundInApiException extends RuntimeException {
    public NoCurrencyFoundInApiException() {
        super("Currency not found!");
    }
}
