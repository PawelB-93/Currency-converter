package com.example.Currencyconverter.repository;

import com.example.Currencyconverter.model.Currency;
import com.example.Currencyconverter.model.CurrencyEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface CurrencyRepository extends CrudRepository<CurrencyEntity, Integer> {
    public Optional<CurrencyEntity> findByFirstCurrencyAndSecondCurrencyAndDate(String firstCurrency, String secondCurrency, LocalDate date);
}
