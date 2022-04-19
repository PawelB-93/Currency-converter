package com.example.Currencyconverter.service;

import com.example.Currencyconverter.model.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class ExchangeRateApi {

    private final RestTemplate restTemplate;

    @Autowired
    public ExchangeRateApi(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private final String CURRENCY_WITH_DATE = "https://api.exchangerate.host/convert?from=%s&to=%s&date=%s";

    public Currency getCurrency(String firstCurrency, String secondCurrency, String date) throws RestClientException {
        String url = String.format(CURRENCY_WITH_DATE, firstCurrency, secondCurrency, date);
        return restTemplate.getForObject(url, Currency.class);
    }


}
