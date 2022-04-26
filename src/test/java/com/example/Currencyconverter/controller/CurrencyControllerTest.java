package com.example.Currencyconverter.controller;

import com.example.Currencyconverter.exception.IncorrectDateFormatException;
import com.example.Currencyconverter.exception.NoCurrencyFoundInApiException;
import com.example.Currencyconverter.exception.NoCurrencyFoundInDatabaseException;
import com.example.Currencyconverter.model.CurrencyDto;
import com.example.Currencyconverter.service.CurrencyService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class CurrencyControllerTest {

    @MockBean
    private CurrencyService currencyService;

    @Autowired
    MockMvc mockMvc;

    @LocalServerPort
    private int port;

    @Test
    void when_getCurrentExchangeRate_is_requested_then_current_rate_should_be_returned() throws Exception {
        //given
        CurrencyDto currencyDto = new CurrencyDto();
        currencyDto.setFirstCurrency("USD");
        currencyDto.setSecondCurrency("PLN");
        currencyDto.setDate(LocalDate.now());
        currencyDto.setResult(1.05);
        currencyDto.setViewCount(1);
        Mockito.when(currencyService.checkCurrency("USD", "PLN", LocalDate.now().toString()))
                .thenReturn(currencyDto);
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:" + port +
                        "/api/current/USD/PLN"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstCurrency", equalTo("USD")))
                .andExpect(jsonPath("$.secondCurrency", equalTo("PLN")))
                .andExpect(jsonPath("$.result", equalTo(1.05)))
                .andExpect(jsonPath("$.date", equalTo(LocalDate.now().toString())))
                .andExpect(jsonPath("$.viewCount", equalTo(1)));
    }

    @Test
    void when_getCurrentExchangeRate_is_requested_and_current_rate_does_not_exist_then_error_should_be_returned() throws Exception {
        //given
        Mockito.when(currencyService.checkCurrency("USD", "PLN", LocalDate.now().toString()))
                .thenThrow(NoCurrencyFoundInApiException.class);
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:" + port +
                        "/api/current/USD/PLN"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void when_historicalExchangeRate_is_requested_then_historical_rate_should_be_returned() throws Exception {
        //given
        CurrencyDto currencyDto = new CurrencyDto();
        currencyDto.setFirstCurrency("USD");
        currencyDto.setSecondCurrency("PLN");
        currencyDto.setDate(LocalDate.of(2022, 4, 15));
        currencyDto.setResult(1.10);
        currencyDto.setViewCount(1);
        Mockito.when(currencyService.checkCurrency("USD", "PLN", "2022-04-15"))
                .thenReturn(currencyDto);
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:" + port +
                        "/api/historical/USD/PLN/2022-04-15"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstCurrency", equalTo("USD")))
                .andExpect(jsonPath("$.secondCurrency", equalTo("PLN")))
                .andExpect(jsonPath("$.result", equalTo(1.10)))
                .andExpect(jsonPath("$.date", equalTo("2022-04-15")))
                .andExpect(jsonPath("$.viewCount", equalTo(1)));
    }

    @Test
    void when_historicalExchangeRate_is_requested_and_historical_rate_does_not_exist_then_error_should_be_returned() throws Exception {
        //given
        Mockito.when(currencyService.checkCurrency("USD", "PLN", "2022-03-15"))
                .thenThrow(NoCurrencyFoundInApiException.class);
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:" + port +
                        "/api/historical/USD/PLN/2022-03-15"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void when_getStatisticalExchangeRate_is_requested_then_statistical_rate_should_be_returned() throws Exception {
        //given
        CurrencyDto currencyDto1 = new CurrencyDto();
        currencyDto1.setFirstCurrency("USD");
        currencyDto1.setSecondCurrency("PLN");
        currencyDto1.setDate(LocalDate.of(2022, 3, 15));
        currencyDto1.setResult(1.05);
        currencyDto1.setViewCount(1);

        CurrencyDto currencyDto2 = new CurrencyDto();
        currencyDto2.setFirstCurrency("USD");
        currencyDto2.setSecondCurrency("PLN");
        currencyDto2.setDate(LocalDate.of(2022, 3, 16));
        currencyDto2.setResult(1.10);
        currencyDto2.setViewCount(2);
        List<CurrencyDto> currencyDtoList = List.of(currencyDto1, currencyDto2);
        //when
        Mockito.when(currencyService.checkCurrencyHistoricalInterval("USD", "PLN", "2022-03-15", "2022-03-16"))
                .thenReturn(currencyDtoList);
        //then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("http://localhost:" + port +
                                "/api/historical-interval/USD/PLN/2022-03-15/2022-03-16")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstCurrency", equalTo("USD")))
                .andExpect(jsonPath("$[1].secondCurrency", equalTo("PLN")))
                .andExpect(jsonPath("$[0].date", equalTo("2022-03-15")))
                .andExpect(jsonPath("$[1].date", equalTo("2022-03-16")))
                .andExpect(jsonPath("$[0].result", equalTo(1.05)))
                .andExpect(jsonPath("$[1].result", equalTo(1.10)));
    }

    @Test
    void when_getStatisticalExchangeRate_is_requested_and_statistical_rate_does_not_exist_then_error_should_be_returned() throws Exception {
        //given
        Mockito.when(currencyService.checkCurrencyHistoricalInterval("USD", "PLN", "2022-03-15", "2022-03-16"))
                .thenThrow(NoCurrencyFoundInApiException.class);
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:" + port +
                        "/api/historical-interval/USD/PLN/2022-03-15/2022-03-16"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void when_deleteCurrency_is_requested_then_currency_to_delete_should_be_returned() throws Exception {
        //given
        CurrencyDto currencyDto = new CurrencyDto();
        currencyDto.setFirstCurrency("USD");
        currencyDto.setSecondCurrency("PLN");
        currencyDto.setDate(LocalDate.of(2022, 3, 15));
        currencyDto.setResult(1.10);
        currencyDto.setViewCount(1);
        Mockito.when(currencyService.deleteCurrency("USD", "PLN", "2022-03-15")).thenReturn(currencyDto);
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("http://localhost:" + port +
                                "/api/delete/USD/PLN/2022-03-15")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstCurrency", equalTo("USD")))
                .andExpect(jsonPath("$.secondCurrency", equalTo("PLN")))
                .andExpect(jsonPath("$.result", equalTo(1.10)))
                .andExpect(jsonPath("$.date", equalTo("2022-03-15")))
                .andExpect(jsonPath("$.viewCount", equalTo(1)));
    }

    @Test
    void when_deleteCurrency_is_requested_and_statistical_rate_does_not_exist_then_error_should_be_returned() throws Exception {
        //given
        Mockito.when(currencyService.deleteCurrency("USD", "PLN", "2022-03-15")).thenThrow(NoCurrencyFoundInDatabaseException.class);
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.delete("http://localhost:" + port +
                        "/api/delete/USD/PLN/2022-03-15"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void when_date_format_is_incorrect_then_error_should_be_returned() throws Exception {
        //given
        Mockito.when(currencyService.checkCurrency("USD", "PLN", "202-03-15")).thenThrow(IncorrectDateFormatException.class);
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:" + port +
                        "/api/historical/USD/PLN/202-03-15"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

}