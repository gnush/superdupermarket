package code.challenge;

import code.challenge.currency.Currency;
import code.challenge.currency.EUR;
import code.challenge.currency.USD;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
public class Assertions {
    public static void assertCloseTo(BigDecimal expected, BigDecimal actual, double allowedDelta) {
        if (expected.compareTo(actual.add(BigDecimal.valueOf(allowedDelta))) > 0
                || expected.compareTo(actual.subtract(BigDecimal.valueOf(allowedDelta))) < 0)
            org.junit.jupiter.api.Assertions.fail();
    }

    public static void assertCloseTo(Currency expected, Currency actual, double allowedDelta) {
        assertEquals(expected.getClass(), actual.getClass());

        switch (actual) {
            case EUR eur -> assertCloseTo(((EUR) expected).amount(), eur.amount(), allowedDelta);
            case USD usd -> assertCloseTo(((USD) expected).amount(), usd.amount(), allowedDelta);
        }
    }
}
