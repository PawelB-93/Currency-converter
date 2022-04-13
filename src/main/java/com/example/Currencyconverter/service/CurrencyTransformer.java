package com.example.Currencyconverter.service;

import com.example.Currencyconverter.model.CurrencyDto;
import com.example.Currencyconverter.model.CurrencyEntity;
import com.example.Currencyconverter.model.Currency;
import org.springframework.stereotype.Component;

@Component
public class CurrencyTransformer {
    public CurrencyEntity toEntity(Currency currency) {
        CurrencyEntity currencyEntity = new CurrencyEntity();
        currencyEntity.setFirstCurrency(currency.getQuery().getFirstCurrency());
        currencyEntity.setSecondCurrency(currency.getQuery().getSecondCurrency());
        currencyEntity.setDate(currency.getDate());
        currencyEntity.setResult(currency.getResult());
        return currencyEntity;
    }

    public CurrencyDto apiToDto(Currency currency) {
        CurrencyDto currencyDto = new CurrencyDto();
        currencyDto.setFirstCurrency(currency.getQuery().getFirstCurrency());
        currencyDto.setSecondCurrency(currency.getQuery().getSecondCurrency());
        currencyDto.setDate(currency.getDate());
        currencyDto.setResult(currency.getResult());
        return currencyDto;
    }

    public CurrencyDto entityToDto(CurrencyEntity currencyEntity) {
        CurrencyDto currencyDto = new CurrencyDto();
        currencyDto.setFirstCurrency(currencyEntity.getFirstCurrency());
        currencyDto.setSecondCurrency(currencyEntity.getSecondCurrency());
        currencyDto.setDate(currencyEntity.getDate());
        currencyDto.setResult(currencyEntity.getResult());
        return currencyDto;
    }
}
