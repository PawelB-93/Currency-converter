package com.example.Currencyconverter.exception;

import java.time.DateTimeException;

public class IncorrectDateFormatException extends DateTimeException {
    public IncorrectDateFormatException() {
        super("Incorrect date format!");
    }
}
