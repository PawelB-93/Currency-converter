package com.example.Currencyconverter.service;

import com.example.Currencyconverter.exception.IncorrectDateFormatException;
import com.example.Currencyconverter.exception.NoCurrencyFoundInApiException;
import com.example.Currencyconverter.exception.NoCurrencyFoundInDatabaseException;
import com.example.Currencyconverter.model.Currency;
import com.example.Currencyconverter.model.CurrencyDto;
import com.example.Currencyconverter.model.CurrencyEntity;
import com.example.Currencyconverter.model.CurrencyType;
import com.example.Currencyconverter.repository.CurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class CurrencyServiceTest {

    @TestConfiguration
    static class CurrencyServiceTestConfiguration {
        @Bean
        CurrencyService currencyService(CurrencyRepository currencyRepository, ExchangeRateApi exchangeRateApi) {
            return new CurrencyService(exchangeRateApi, new CurrencyTransformer(), currencyRepository);
        }
    }

    @BeforeEach
    void init() {
        currencyEntity = new CurrencyEntity();
        currencyEntity.setId(1);
        currencyEntity.setFirstCurrency("USD");
        currencyEntity.setSecondCurrency("EUR");
        currencyEntity.setDate(LocalDate.of(2022, 4, 15));
        currencyEntity.setResult(1.15);
        currencyEntity.setViewCount(0);

        currency = new Currency();
        currency.setDate(LocalDate.of(2022, 4, 15));
        currency.setResult(1.15);
        CurrencyType currencyType = new CurrencyType();
        currencyType.setFirstCurrency("USD");
        currencyType.setSecondCurrency("EUR");
        currency.setCurrencyType(currencyType);
    }

    @MockBean
    CurrencyRepository currencyRepository;

    @MockBean
    ExchangeRateApi exchangeRateApi;

    @Autowired
    CurrencyService currencyService;

    private CurrencyEntity currencyEntity;

    private Currency currency;

    @Test
    void when_checkCurrency_is_used_and_currency_is_already_in_db_then_currency_from_db_should_be_returned() {
        //given
        Mockito.when(currencyRepository.findByFirstCurrencyAndSecondCurrencyAndDate("USD", "EUR", LocalDate.of(2022, 5, 15)))
                .thenReturn(Optional.of(currencyEntity));
        //when
        CurrencyDto currencyDto = currencyService.checkCurrency("USD", "EUR", "2022-05-15");
        //then
        assertEquals("EUR", currencyDto.getSecondCurrency());
    }

    @Test
    void when_checkCurrency_is_used_and_currency_is_not_found_in_db_then_currency_from_api_should_be_returned() {
        //given
        Mockito.when(currencyRepository.findByFirstCurrencyAndSecondCurrencyAndDate("USD", "EUR", LocalDate.of(2022, 5, 15)))
                .thenReturn(Optional.empty());
        Mockito.when(exchangeRateApi.getCurrency("USD", "EUR", "2022-05-15")).thenReturn(currency);
        //when
        CurrencyDto currencyDto = currencyService.checkCurrency("USD", "EUR", "2022-05-15");
        //then
        assertEquals("EUR", currencyDto.getSecondCurrency());
    }

    @Test
    void when_checkCurrency_is_used_and_the_currency_result_equals_0_then_exception_should_be_thrown() {
        //given
        Currency currency = new Currency();
        currency.setDate(LocalDate.of(2022, 4, 15));
        currency.setResult(0);
        CurrencyType currencyType = new CurrencyType();
        currencyType.setFirstCurrency("USD");
        currencyType.setSecondCurrency("EUR");
        currency.setCurrencyType(currencyType);

        Mockito.when(currencyRepository.findByFirstCurrencyAndSecondCurrencyAndDate("USD", "EUR", LocalDate.of(2022, 5, 15)))
                .thenReturn(Optional.empty());
        Mockito.when(exchangeRateApi.getCurrency("USD", "EUR", "2022-05-15")).thenReturn(currency);
        //when
        //then
        assertThrows(NoCurrencyFoundInApiException.class, () -> {
            currencyService.checkCurrency("USD", "EUR", "2022-05-15");
        });
    }

    @Test
    void when_deleteCurrency_is_used_and_currency_exist_then_currency_should_be_deleted() {
        //given
        Mockito.when(currencyRepository.findByFirstCurrencyAndSecondCurrencyAndDate("USD", "EUR", LocalDate.of(2022, 4, 15)))
                .thenReturn(Optional.of(currencyEntity));
        //when
        CurrencyDto currencyDto = currencyService.deleteCurrency("USD", "EUR", "2022-04-15");
        //then
        assertNotNull(currencyDto);
    }

    @Test
    void when_deleteCurrency_is_used_and_currency_does_not_exist_then_exception_should_be_thrown() {
        //given
        Mockito.when(currencyRepository.findByFirstCurrencyAndSecondCurrencyAndDate("USD", "EUR", LocalDate.of(2022, 4, 15)))
                .thenReturn(Optional.empty());
        //when
        //then
        assertThrows(NoCurrencyFoundInDatabaseException.class, () -> {
            currencyService.deleteCurrency("USD", "EUR", "2022-04-15");
        });
    }

    @Test
    void when_stringToLocalDate_is_used_and_string_is_valid_then_localDate_should_be_returned() {
        //given
        String date = "2022-03-20";
        //when
        LocalDate resultDate = currencyService.stringToLocalDate(date);
        //then
        assertEquals(LocalDate.of(2022, 3, 20), resultDate);
    }

    @Test
    void when_stringToLocalDate_is_used_and_string_is_not_valid_then_exception_should_be_thrown() {
        //given
        String date = "202-03-20";
        //when
        //then
        assertThrows(IncorrectDateFormatException.class, (() -> {
            currencyService.stringToLocalDate(date);
        }));
    }

    @Test
    void when_getDatesInterval_is_used_then_list_of_dates_should_be_returned() {
        //given
        //when
        List<LocalDate> dates = currencyService.getDatesInterval(LocalDate.of(2022, 3, 15), LocalDate.of(2022, 3, 20));
        //then
        assertAll(
                () -> assertEquals(6, dates.size()),
                () -> assertEquals(LocalDate.of(2022, 3, 15), dates.get(0)),
                () -> assertEquals(LocalDate.of(2022, 3, 17), dates.get(2)),
                () -> assertEquals(LocalDate.of(2022, 3, 20), dates.get(dates.size() - 1))
        );
    }

    @Test
    void when_saveAndUpdate_is_used_then_updated_currencyEntity_should_be_returned() {
        //given
        //when
        CurrencyEntity resultCurrency = currencyService.saveAndUpdate(currencyEntity);
        //then
        assertAll(
                () -> assertNotNull(resultCurrency),
                () -> assertEquals(1, resultCurrency.getViewCount())
        );
    }

}