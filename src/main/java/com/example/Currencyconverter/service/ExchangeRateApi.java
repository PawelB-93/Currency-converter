package com.example.Currencyconverter.service;

import com.example.Currencyconverter.model.Currency;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ExchangeRateApi {

    private final RestTemplate restTemplate = new RestTemplate();

    public Currency getCurrency(String firstCurrency, String secondCurrency, String date) {
        String url = String.format("https://api.exchangerate.host/convert?from=%s&to=%s&date=%s", firstCurrency, secondCurrency, date);
        Currency currency = restTemplate.getForObject(url, Currency.class);
        return currency;
    }
}
