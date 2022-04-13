package com.example.Currencyconverter.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class Currency {
    private LocalDate date;
    private double result;
    private Query query;
}

