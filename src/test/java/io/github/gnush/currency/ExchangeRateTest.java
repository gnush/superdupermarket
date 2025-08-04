package io.github.gnush.currency;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ExchangeRateTest {
    @AfterEach
    void cleanUpCache() {
        ExchangeRateService.clearCache();
    }

    @Test
    void exchangeRateEurToUsdOn2000_01_01() {
        assertTrue(
                ExchangeRateService.exchangeRate(
                        LocalDate.of(2000, 1, 1),
                        "EUR",
                        "USD"
                ).isPresent()
        );
    }

    @Test
    void exchangeRateUsdToEurOn2000_12_31() {
        assertTrue(
                ExchangeRateService.exchangeRate(
                        LocalDate.of(2000, 12, 31),
                        "USD",
                        "EUR"
                ).isPresent()
        );
    }

    @Test
    void noExchangeToNonexistingCurrency() {
        assertTrue(
                ExchangeRateService.exchangeRate(
                        LocalDate.of(2000, 1, 1),
                        "EUR",
                        "FAKE"
                ).isEmpty()
        );
    }

    @Test
    void noExchangeFromNonexistingCurrency() {
        assertTrue(
                ExchangeRateService.exchangeRate(
                        LocalDate.of(2000, 1, 1),
                        "FAKE",
                        "EUR"
                ).isEmpty()
        );
    }

    @Test
    void exchangeFromEmptyCurrenciesDefaultsToEUR() {
        assertTrue(
                ExchangeRateService.exchangeRate(
                        LocalDate.of(2000, 1, 1),
                        "",
                        "USD"
                ).isPresent()
        );
    }

    @Test
    void cachingExchangeRate() {
        assertTrue(
                ExchangeRateService.exchangeRate(
                        LocalDate.of(2000, 1, 1),
                        "EUR",
                        "USD"
                ).isPresent()
        );

        assertTrue(
                ExchangeRateService.exchangeRate(
                        LocalDate.of(2000, 1, 1),
                        "EUR",
                        "USD"
                ).isPresent()
        );

        assertTrue(
                ExchangeRateService.exchangeRate(
                        LocalDate.of(2000, 1, 1),
                        "EUR",
                        "GBP"
                ).isPresent()
        );
    }

    @Test
    void exchangeEurToUsdAndGbp(){
        Map<String, BigDecimal> actual = ExchangeRateService.exchangeRates(
                LocalDate.of(2000, 1, 1),
                "EUR",
                List.of("USD", "GBP")
        );

        assertTrue(actual.containsKey("USD"));
        assertTrue(actual.containsKey("GBP"));
    }

    @Test
    void exchangeEurToNonExistingCurrency(){
        Map<String, BigDecimal> actual = ExchangeRateService.exchangeRates(
                LocalDate.of(2000, 1, 1),
                "EUR",
                List.of("FAKE")
        );

        System.out.println(actual);

        assertTrue(actual.isEmpty());
    }

    @Test
    void exchangeEurToEmptyListDefaultsToAll(){
        Map<String, BigDecimal> actual = ExchangeRateService.exchangeRates(
                LocalDate.of(2000, 1, 1),
                "EUR",
                Collections.emptyList()
        );

        System.out.println(actual);

        assertTrue(actual.size() > 5);
        assertTrue(actual.containsKey("USD"));
        assertTrue(actual.containsKey("GBP"));
        assertTrue(actual.containsKey("AUD"));
    }

    @Test
    void notUsingCacheWhenNewExchangesAreRequestedForACurrency() {
        Map<String, BigDecimal> first = Map.copyOf(
                ExchangeRateService.exchangeRates(
                        LocalDate.of(2000, 1, 1),
                        "EUR",
                        List.of("USD", "GBP")
                )
        );

        Map<String, BigDecimal> second = Map.copyOf(
                ExchangeRateService.exchangeRates(
                        LocalDate.of(2000, 1, 1),
                        "EUR",
                        List.of("USD", "GBP", "AUD")
                )
        );

        Map<String, BigDecimal> third = ExchangeRateService.exchangeRates(
                LocalDate.of(2000, 1, 1),
                "EUR",
                List.of("USD", "GBP", "AUD", "CAD")
        );

        assertEquals(2, first.size());
        assertTrue(first.containsKey("USD"));
        assertTrue(first.containsKey("GBP"));

        assertEquals(3, second.size());
        assertTrue(second.containsKey("USD"));
        assertTrue(second.containsKey("GBP"));
        assertTrue(second.containsKey("AUD"));

        assertEquals(4, third.size());
        assertTrue(third.containsKey("USD"));
        assertTrue(third.containsKey("GBP"));
        assertTrue(third.containsKey("AUD"));
        assertTrue(third.containsKey("CAD"));
    }

    @Test
    void notUsingCacheWhenExchangingNewCurrency() {
        Optional<BigDecimal> first = ExchangeRateService.exchangeRate(
                LocalDate.of(2000, 1, 1),
                "EUR",
                "USD"
        );

        Optional<BigDecimal> second = ExchangeRateService.exchangeRate(
                LocalDate.of(2000, 1, 1),
                "USD",
                "EUR"
        );

        assertTrue(first.isPresent());
        assertTrue(second.isPresent());
        assertNotEquals(first, second);
    }

    @Test
    void usingCacheWhenFewerExchangesAreRequestedForACurrency() {
        Map<String, BigDecimal> first = Map.copyOf(
                ExchangeRateService.exchangeRates(
                        LocalDate.of(2000, 1, 1),
                        "EUR",
                        List.of("USD", "GBP", "AUD")
                )
        );

        Map<String, BigDecimal> second = ExchangeRateService.exchangeRates(
                LocalDate.of(2000, 1, 1),
                "EUR",
                List.of("USD", "GBP")
        );

        assertEquals(3, first.size());
        assertTrue(first.containsKey("USD"));
        assertTrue(first.containsKey("GBP"));
        assertTrue(first.containsKey("AUD"));

        assertEquals(
                first,
                second
        );
    }
}
