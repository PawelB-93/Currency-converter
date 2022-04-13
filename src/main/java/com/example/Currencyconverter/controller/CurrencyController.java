package com.example.Currencyconverter.controller;

import com.example.Currencyconverter.model.CurrencyDto;
import com.example.Currencyconverter.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CurrencyController {
    private final CurrencyService currencyService;

    @Autowired
    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }
    //    @PostMapping
//    public void getCurrency(@RequestParam String in, @RequestParam String out) {
//        System.out.println(exchangeRateApi.getCurrency(in,out));
//    }

    @GetMapping
    public ResponseEntity<CurrencyDto> getCurrency(@RequestParam String firstCurrency, @RequestParam String secondCurrency, @RequestParam(required = false) String date) {
        return ResponseEntity.ok(currencyService.checkCurrency(firstCurrency, secondCurrency, date));
    }
}
