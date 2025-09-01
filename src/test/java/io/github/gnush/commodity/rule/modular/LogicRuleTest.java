package io.github.gnush.commodity.rule.modular;

import io.github.gnush.commodity.Commodity;
import io.github.gnush.commodity.ExpirationDate;
import io.github.gnush.commodity.rule.GeneralRules;
import io.github.gnush.commodity.rule.modular.lit.Literal;
import io.github.gnush.commodity.rule.modular.logic.*;
import io.github.gnush.currency.EUR;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
public class LogicRuleTest {
    Commodity commodity1 = null;
    Commodity commodity2 = null;

    @BeforeEach
    void setCommodity() {
        commodity1 = Commodity.of(
                "Test",
                new EUR(BigDecimal.ONE),
                1,
                new GeneralRules()
        ).orElseThrow(
                () -> new AssertionFailedError("@Before commodity1 creation failed")
        );

        commodity2 = Commodity.of(
                "Also Test",
                new EUR(BigDecimal.ONE),
                2,
                new ExpirationDate.ExpiresAt(LocalDate.of(1900, 1, 1)),
                new GeneralRules()
        ).orElseThrow(
                () -> new AssertionFailedError("@Before commodity2 creation failed")
        );
    }

    @Test
    void trueLit() {
        assertTrue(Literal.of(true).apply(1));
    }

    @Test
    void falseLit() {
        assertFalse(Literal.of(false).apply(1));
    }

    @Test
    void notTrue() {
        assertTrue(new Not(Literal.of(false)).apply(commodity1));
    }

    @Test
    void notFalse() {
        assertFalse(new Not(Literal.of(true)).apply(commodity1));
    }

    @Test
    void andTrue() {
        assertTrue(new And(Literal.of(true), Literal.of(true)).apply(commodity1));
    }

    @Test
    void andFalse() {
        assertFalse(new And(Literal.of(false), Literal.of(false)).apply(commodity1));
        assertFalse(new And(Literal.of(false), Literal.of(true)) .apply(commodity1));
        assertFalse(new And(Literal.of(true) , Literal.of(false)).apply(commodity1));
    }

    @Test
    void orTrue() {
        assertTrue(new Or(Literal.of(true) , Literal.of(true)) .apply(commodity1));
        assertTrue(new Or(Literal.of(false), Literal.of(true)) .apply(commodity1));
        assertTrue(new Or(Literal.of(true) , Literal.of(false)).apply(commodity1));
    }

    @Test
    void orFalse() {
        assertFalse(new Or(Literal.of(false), Literal.of(false)).apply(commodity1));
    }

    @Test
    void impliesTrue() {
        assertTrue(new Implies(Literal.of(false), Literal.of(false)).apply(commodity1));
        assertTrue(new Implies(Literal.of(false), Literal.of(true)) .apply(commodity1));
        assertTrue(new Implies(Literal.of(true) , Literal.of(true)) .apply(commodity1));
    }

    @Test
    void impliesFalse() {
        assertFalse(new Implies(Literal.of(true), Literal.of(false)).apply(commodity1));
    }

    @Test
    void qualityEqualsTrue() {
        assertTrue(Equals.quality(1).apply(commodity1));
    }

    @Test
    void qualityEqualsFalse() {
        assertFalse(Equals.quality(commodity2.getQuality()).apply(commodity1));
    }

    @Test
    void expirationDateEqualsTrue() {
        assertTrue(Equals.expirationDate(ExpirationDate.DoesNotExpire.instance()).apply(commodity1));
    }

    @Test
    void expirationDateEqualsFalse() {
        assertFalse(Equals.expirationDate(commodity2.expirationDate).apply(commodity1));
    }

    @Test
    void labelEqualsTrue() {
        assertTrue(Equals.label("Test").apply(commodity1));
    }

    @Test
    void labelEqualsFalse() {
        assertFalse(Equals.label("Test").apply(commodity2));
    }
}
