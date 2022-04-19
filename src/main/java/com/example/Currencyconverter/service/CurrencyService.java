package com.example.Currencyconverter.service;

import com.example.Currencyconverter.exception.IncorrectDateFormatException;
import com.example.Currencyconverter.exception.NoCurrencyFoundInApiException;
import com.example.Currencyconverter.exception.NoCurrencyFoundInDatabaseException;
import com.example.Currencyconverter.model.CurrencyDto;
import com.example.Currencyconverter.model.CurrencyEntity;
import com.example.Currencyconverter.repository.CurrencyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
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
            return currencyTransformer.entityToDto(saveAndUpdate(currencyEntity.get()));
        }
        try {
            return currencyTransformer.entityToDto(saveAndUpdate(currencyTransformer.toEntity(exchangeRateApi.getCurrency(firstCurrency, secondCurrency, date))));
        } catch (RuntimeException e) {
            throw new NoCurrencyFoundInApiException();
        }
    }

    public List<CurrencyDto> checkCurrencyHistoricalInterval(String base, String target, String from, String to) {
        List<LocalDate> localDateList = getDatesInterval(LocalDate.parse(from), LocalDate.parse(to));
        return localDateList.stream().map(localDate -> checkCurrency(base, target, localDate.toString())).collect(Collectors.toList());
    }

    public CurrencyDto deleteCurrency(String firstCurrency, String secondCurrency, String date) {
        Optional<CurrencyEntity> currencyToDelete = currencyRepository.findByFirstCurrencyAndSecondCurrencyAndDate(firstCurrency, secondCurrency, stringToLocalDate(date));
        currencyToDelete.ifPresent(currencyRepository::delete);
        return currencyTransformer.entityToDto(currencyToDelete.orElseThrow(() -> {
            throw new NoCurrencyFoundInDatabaseException();
        }));
    }


    public List<LocalDate> getDatesInterval(LocalDate startDate, LocalDate endDate) {
        return startDate.datesUntil(endDate)
                .collect(Collectors.toList());
    }

    public LocalDate stringToLocalDate(String date) {
        try {
            return LocalDate.parse(date);
        } catch (DateTimeException e) {
            throw new IncorrectDateFormatException();
        }
    }

    public CurrencyEntity saveAndUpdate(CurrencyEntity currencyEntity) {
        currencyEntity.setViewCount(currencyEntity.getViewCount() + 1);
        currencyRepository.save(currencyEntity);
        return currencyEntity;
    }
}
