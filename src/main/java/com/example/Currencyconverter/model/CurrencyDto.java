package com.example.Currencyconverter.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class CurrencyDto {
    private String firstCurrency;
    private String secondCurrency;
    private LocalDate date;
    private double result;
    private int viewCount;
}
