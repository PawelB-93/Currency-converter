package com.example.Currencyconverter.service;

import com.example.Currencyconverter.model.Currency;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ExchangeRateApi {

    private final RestTemplate restTemplate = new RestTemplate();

    private final String CURRENCY_WITH_DATE = "https://api.exchangerate.host/convert?from=%s&to=%s&date=%s";
    private final String CURRENCY_WITHOUT_DATE = "https://api.exchangerate.host/convert?from=%s&to=%s";

    public Currency getCurrency(String firstCurrency, String secondCurrency, String date) {
        String url = date != null ? CURRENCY_WITH_DATE : CURRENCY_WITHOUT_DATE;
        return restTemplate.getForObject(String.format(url, firstCurrency, secondCurrency, date), Currency.class);
    }
}
