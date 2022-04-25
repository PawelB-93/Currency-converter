package com.example.Currencyconverter.service;

import com.example.Currencyconverter.model.Currency;
import com.example.Currencyconverter.model.CurrencyDto;
import com.example.Currencyconverter.model.CurrencyEntity;
import com.example.Currencyconverter.model.CurrencyType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyTransformerTest {

    private final CurrencyTransformer currencyTransformer = new CurrencyTransformer();

    @Test
    void when_toEntity_is_used_then_currencyEntity_should_be_returned() {
        //given
        Currency currency = new Currency();
        currency.setDate(LocalDate.of(2022, 4, 15));
        currency.setResult(1.15);
        CurrencyType currencyType = new CurrencyType();
        currencyType.setFirstCurrency("USD");
        currencyType.setSecondCurrency("EUR");
        currency.setCurrencyType(currencyType);
        //when
        CurrencyEntity currencyEntity = currencyTransformer.toEntity(currency);
        //then
        assertAll(
                () -> assertNotNull(currencyEntity),
                () -> assertEquals("EUR", currencyEntity.getSecondCurrency()),
                () -> assertEquals(LocalDate.of(2022, 4, 15), currencyEntity.getDate())
        );
    }

    @Test
    void when_entityToDto_is_used_then_currencyDto_should_be_returned() {
        //given
        CurrencyEntity currencyEntity = new CurrencyEntity();
        currencyEntity.setId(1);
        currencyEntity.setFirstCurrency("USD");
        currencyEntity.setSecondCurrency("EUR");
        currencyEntity.setDate(LocalDate.of(2022, 4, 15));
        currencyEntity.setResult(1.15);
        currencyEntity.setViewCount(0);
        //when
        CurrencyDto currencyDto=currencyTransformer.entityToDto(currencyEntity);
        //then
        assertAll(
                () -> assertNotNull(currencyDto),
                () -> assertEquals("EUR", currencyEntity.getSecondCurrency()),
                () -> assertEquals(LocalDate.of(2022, 4, 15), currencyEntity.getDate())
        );
    }
}