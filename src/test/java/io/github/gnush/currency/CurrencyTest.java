package io.github.gnush.currency;

import io.github.gnush.SimulationContext;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class CurrencyTest {
    final BigDecimal one = new BigDecimal("1");
    final BigDecimal two = new BigDecimal("2");
    final BigDecimal three = new BigDecimal("3");
    final BigDecimal oneDotFive = new BigDecimal("1.5");

    @Test
    void eurToString() {
        assertEquals(
                "1.5 €",
                new EUR(oneDotFive).toString()
        );
    }

    @Test
    void usdToString() {
        assertEquals(
                "$1.5",
                new USD(oneDotFive).toString()
        );
    }

    @Test
    void eurEquals() {
        assertEquals(
                new EUR(new BigDecimal("1.0")),
                new EUR(new BigDecimal("1.0"))
        );

        assertEquals(
                new EUR(new BigDecimal("1.00")),
                new EUR(new BigDecimal("1.0"))
        );

        assertEquals(
                new EUR(new BigDecimal("1.0")),
                new EUR(new BigDecimal("1.00"))
        );
    }

    @Test
    void usdEquals() {
        assertEquals(
                new USD(new BigDecimal("1.0")),
                new USD(new BigDecimal("1.0"))
        );

        assertEquals(
                new USD(new BigDecimal("1.00")),
                new USD(new BigDecimal("1.0"))
        );

        assertEquals(
                new USD(new BigDecimal("1.0")),
                new USD(new BigDecimal("1.00"))
        );
    }

    @Test
    void eurNotEquals() {
        assertNotEquals(
                new EUR(one),
                new EUR(two)
        );
    }

    @Test
    void usdNotEquals() {
        assertNotEquals(
                new USD(one),
                new USD(two)
        );
    }

    @Test
    void eurNotEqualToUsd() {
        assertNotEquals(new EUR(one), new USD(one));
    }

    @Test
    void usdNotEqualToEur() {
        assertNotEquals(new USD(one), new EUR(one));
    }

    @Test
    void addBigDecimalToEur() {
        assertEquals(
                new EUR(two),
                new EUR(one).add(one)
        );
    }

    @Test
    void addBigDecimalToUsd() {
        assertEquals(
                new USD(two),
                new USD(one).add(one)
        );
    }

    @Test
    void addEurToEur() {
        assertEquals(
                new EUR(three),
                new EUR(oneDotFive).add(new EUR(oneDotFive))
        );
    }

    @Test
    void addUsdToUsd() {
        assertEquals(
                new USD(three),
                new USD(oneDotFive).add(new USD(oneDotFive))
        );
    }

    @Test
    void addEurToUsd() {
        SimulationContext.setClock(LocalDate.of(2000, 1, 1));

        try (MockedStatic<ExchangeRateService> exchangeRateService = Mockito.mockStatic(ExchangeRateService.class)){
            exchangeRateService.when(() ->
                    ExchangeRateService.exchangeRate(
                            LocalDate.of(2000, 1, 1),
                            "EUR",
                            "USD"))
                    .thenReturn(Optional.of(BigDecimal.TWO));

            assertEquals(
                    new USD(new BigDecimal("3")),
                    new USD(one).add(new EUR(one))
            );
        }
    }

    @Test
    void addUsdToEur() {
        SimulationContext.setClock(LocalDate.of(2000, 1, 1));

        try (MockedStatic<ExchangeRateService> exchangeRateService = Mockito.mockStatic(ExchangeRateService.class)){
            exchangeRateService.when(() ->
                            ExchangeRateService.exchangeRate(
                                    LocalDate.of(2000, 1, 1),
                                    "USD",
                                    "EUR"))
                    .thenReturn(Optional.of(BigDecimal.TWO));

            assertEquals(
                    new EUR(new BigDecimal("4")),
                    new EUR(one).add(new USD(oneDotFive))
            );
        }
    }

    @Test
    void addEurToUSDCurrencyExchangeFails() {
        SimulationContext.setClock(LocalDate.of(2000, 1, 1));

        try (MockedStatic<ExchangeRateService> exchangeRateService = Mockito.mockStatic(ExchangeRateService.class)){
            exchangeRateService.when(() ->
                            ExchangeRateService.exchangeRate(
                                    LocalDate.of(2000, 1, 1),
                                    "USD",
                                    "EUR"))
                    .thenReturn(Optional.empty());

            assertThrows(
                    IllegalArgumentException.class,
                    () -> new USD(one).add(new EUR(one)),
                    "Cannot exchange EUR to USD"
            );
        }
    }

    @Test
    void addUsdToEurCurrencyExchangeFails() {
        SimulationContext.setClock(LocalDate.of(2000, 1, 1));

        try (MockedStatic<ExchangeRateService> exchangeRateService = Mockito.mockStatic(ExchangeRateService.class)){
            exchangeRateService.when(() ->
                            ExchangeRateService.exchangeRate(
                                    LocalDate.of(2000, 1, 1),
                                    "EUR",
                                    "USD"))
                    .thenReturn(Optional.empty());

            assertThrows(
                    IllegalArgumentException.class,
                    () -> new EUR(one).add(new USD(one)),
                    "Cannot exchange USD to EUR"
            );
        }
    }
}
