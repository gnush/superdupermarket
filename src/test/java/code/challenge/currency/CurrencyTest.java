package code.challenge.currency;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

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
        assertNotEquals(new EUR(1), new USD(1));
    }

    @Test
    void usdNotEqualToEur() {
        assertNotEquals(new USD(1), new EUR(1));
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
        assertThrows(
                UnsupportedOperationException.class,
                () -> new USD(1).add(new EUR(1)),
                "Interchanging currencies is not supported"
        );
    }

    @Test
    void addUsdToEur() {
        assertThrows(
                UnsupportedOperationException.class,
                () -> new EUR(1).add(new USD(1)),
                "Interchanging currencies is not supported"
        );
    }
}
