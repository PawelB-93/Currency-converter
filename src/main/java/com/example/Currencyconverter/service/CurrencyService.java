package com.example.Currencyconverter.service;

import com.example.Currencyconverter.model.CurrencyDto;
import com.example.Currencyconverter.model.CurrencyEntity;
import com.example.Currencyconverter.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class CurrencyService {
    private final ExchangeRateApi exchangeRateApi;
    private final CurrencyTransformer currencyTransformer;
    private final CurrencyRepository currencyRepository;

    @Autowired
    public CurrencyService(ExchangeRateApi exchangeRateApi, CurrencyTransformer currencyTransformer, CurrencyRepository currencyRepository) {
        this.exchangeRateApi = exchangeRateApi;
        this.currencyTransformer = currencyTransformer;
        this.currencyRepository = currencyRepository;
    }

    public CurrencyDto checkCurrency(String firstCurrency, String secondCurrency, String date) {
        Optional<CurrencyEntity> currencyEntity = currencyRepository.findByFirstCurrencyAndSecondCurrencyAndDate(firstCurrency, secondCurrency, stringToLocalDate(date));
        if (currencyEntity.isPresent()) {
            return currencyTransformer.entityToDto(currencyEntity.get());
        }
        currencyRepository.save(currencyTransformer.toEntity(exchangeRateApi.getCurrency(firstCurrency, secondCurrency, date)));
        return currencyTransformer.apiToDto(exchangeRateApi.getCurrency(firstCurrency, secondCurrency, date));
    }

    public LocalDate stringToLocalDate(String date) {
        return LocalDate.parse(date);
    }
}
