package code.challenge;

import code.challenge.currency.Currency;
import code.challenge.currency.EUR;
import code.challenge.currency.USD;

import static org.junit.jupiter.api.Assertions.assertEquals;
public class Assertions {
    public static void assertCloseTo(double expected, double actual, double allowedDelta) {
        if (expected > actual+allowedDelta  || expected < actual-allowedDelta)
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
