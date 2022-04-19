package com.example.Currencyconverter.exception;

import org.springframework.web.client.RestClientException;

public class NoCurrencyFoundInApiException extends RestClientException {
    public NoCurrencyFoundInApiException() {
        super("Currency not found!");
    }
}
