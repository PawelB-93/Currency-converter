package com.example.Currencyconverter.repository;

import com.example.Currencyconverter.model.CurrencyEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class CurrencyRepositoryTest {

    @Autowired
    private CurrencyRepository currencyRepository;

    @DisplayName("Should find exchange rate by first and second currency and date")
    @Test
    void findByFirstCurrencyAndSecondCurrencyAndDate() {
        // given
        CurrencyEntity testEntity = new CurrencyEntity();
        testEntity.setFirstCurrency("USD");
        testEntity.setSecondCurrency("PLN");
        testEntity.setDate(LocalDate.of(2022, 3, 1));

        currencyRepository.save(testEntity);

        // when
        Optional<CurrencyEntity> result = currencyRepository.findByFirstCurrencyAndSecondCurrencyAndDate(
                "USD",
                "PLN",
                LocalDate.of(2022, 3, 1)
        );

        // then
        assertThat(result).isPresent()
                .hasValueSatisfying(r -> assertThat(r).isEqualTo(testEntity));
    }
}