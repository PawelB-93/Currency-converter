package com.example.Currencyconverter.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CurrencyControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("Should get current exchange rate for USD and PLN pair")
    @Order(1)
    @Test
    void getCurrentExchangeRate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("http://localhost:" + port + "/api/current/USD/PLN")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstCurrency").exists())
                .andExpect(jsonPath("$.secondCurrency").exists())
                .andExpect(jsonPath("$.date").exists())
                .andExpect(jsonPath("$.result").isNumber())
                .andExpect(jsonPath("$.viewCount").isNumber());
    }

    @DisplayName("Should get historical (2022-03-15) exchange rate for USD and EUR pair")
    @Order(2)
    @Test
    void getHistoricalExchangeRate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("http://localhost:" + port + "/api/historical/USD/EUR/2022-03-15")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstCurrency").exists())
                .andExpect(jsonPath("$.secondCurrency").exists())
                .andExpect(jsonPath("$.date").isNotEmpty())
                .andExpect(jsonPath("$.result").isNumber())
                .andExpect(jsonPath("$.viewCount").isNumber());
    }

    @DisplayName("Should get exchange rates from 2022-03-11 to 2022-03-14 for USD and PLN pair")
    @Order(3)
    @Test
    void getStatisticalExchangeRate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("http://localhost:" + port +
                                "/api/historical-interval/USD/PLN/2022-03-11/2022-03-14")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[1].firstCurrency", equalTo("USD")))
                .andExpect(jsonPath("$[2].secondCurrency", equalTo("PLN")))
                .andExpect(jsonPath("$[*].date", hasItem("2022-03-13")))
                .andExpect(jsonPath("$[*].result").isArray())
                .andExpect(jsonPath("$[*].viewCount").isArray());
    }

    @DisplayName("Should delete exchange rate (USD/PLN/2022-03-12) from previous test")
    @Order(4)
    @Test
    public void deleteCurrency() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("http://localhost:" + port +
                                "/api/delete/USD/PLN/2022-03-12")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.firstCurrency", equalTo("USD")))
                .andExpect(status().isOk());
    }
}
