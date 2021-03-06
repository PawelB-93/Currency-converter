package com.example.Currencyconverter.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CurrencyType {
    @JsonProperty("from")
    private String firstCurrency;
    @JsonProperty("to")
    private String secondCurrency;
}
