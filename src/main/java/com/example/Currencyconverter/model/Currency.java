package com.example.Currencyconverter.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class Currency {
    private LocalDate date;
    private double result;
    private Query query;
}

