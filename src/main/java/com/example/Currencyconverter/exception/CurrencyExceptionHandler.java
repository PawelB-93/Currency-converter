package com.example.Currencyconverter.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CurrencyExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoCurrencyFoundInDatabaseException.class)
    public ErrorResponse handleNotFoundInDbException(final NoCurrencyFoundInDatabaseException exception) {
        log.error("\u001B[31mCurrency not found in database!\033[0m");
        return new ErrorResponse(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoCurrencyFoundInApiException.class)
    public ErrorResponse handleNotFoundInApiException(final NoCurrencyFoundInApiException exception) {
        log.error("\u001B[31mCurrency not found in api!\033[0m");
        return new ErrorResponse(exception.getMessage());
    }

}
