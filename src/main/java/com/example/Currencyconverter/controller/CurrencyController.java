package com.example.Currencyconverter.controller;

import com.example.Currencyconverter.model.CurrencyDto;
import com.example.Currencyconverter.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api")
public class CurrencyController {
    private final CurrencyService currencyService;

    @Autowired
    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }


    @GetMapping("/current/{base}/{target}")
    public ResponseEntity<CurrencyDto> getCurrentExchangeRate(@PathVariable String base,
                                                              @PathVariable String target) {
        return ResponseEntity.ok().body(currencyService.checkCurrency(
                base,
                target,
                String.valueOf(LocalDate.now())
        ));
    }

    @GetMapping("/historical/{base}/{target}/{date}")
    public ResponseEntity<CurrencyDto> getHistoricalExchangeRate(@PathVariable String base,
                                                                 @PathVariable String target,
                                                                 @PathVariable String date) {
        return ResponseEntity.ok().body(currencyService.checkCurrency(base,
                target,
                date)
        );
    }

    @GetMapping("/historical-interval/{base}/{target}/{from}/{to}")
    public ResponseEntity<List<CurrencyDto>> getStatisticalExchangeRate(@PathVariable String base,
                                                                        @PathVariable String target,
                                                                        @PathVariable String from,
                                                                        @PathVariable String to) {
        return ResponseEntity.ok().body(currencyService.checkCurrencyHistoricalInterval(base,
                target,
                from,
                to)
        );
    }

    @DeleteMapping("/delete/{base}/{target}/{date}")
    public ResponseEntity<CurrencyDto> deleteCurrency(@PathVariable String base,
                                                      @PathVariable String target,
                                                      @PathVariable String date) {
        return ResponseEntity.ok().body(currencyService.deleteCurrency(base,
                target,
                date)
        );
    }
}
