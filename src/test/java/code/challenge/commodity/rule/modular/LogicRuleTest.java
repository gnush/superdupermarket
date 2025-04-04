package code.challenge.commodity.rule.modular;

import code.challenge.commodity.Commodity;
import code.challenge.commodity.ExpirationDate;
import code.challenge.commodity.rule.GeneralRules;
import code.challenge.commodity.rule.modular.logic.*;
import code.challenge.currency.EUR;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
public class LogicRuleTest {
    Commodity commodity1 = null;
    Commodity commodity2 = null;

    @BeforeEach
    void setCommodity() {
        commodity1 = Commodity.of(
                "Test",
                new EUR(1),
                1,
                new GeneralRules()
        ).orElseThrow(
                () -> new AssertionFailedError("@Before commodity1 creation failed")
        );

        commodity2 = Commodity.of(
                "Test",
                new EUR(1),
                2,
                new ExpirationDate.ExpiresAt(LocalDate.of(1900, 1, 1)),
                new GeneralRules()
        ).orElseThrow(
                () -> new AssertionFailedError("@Before commodity2 creation failed")
        );
    }

    @Test
    void True() {
        assertTrue(new True().apply(commodity1));
    }

    @Test
    void False() {
        assertFalse(new False().apply(commodity1));
    }

    @Test
    void NotTrue() {
        assertTrue(new Not(new False()).apply(commodity1));
    }

    @Test
    void NotFalse() {
        assertFalse(new Not(new True()).apply(commodity1));
    }

    @Test
    void AndTrue() {
        assertTrue(new And(new True(), new True()).apply(commodity1));
    }

    @Test
    void AndFalse() {
        assertFalse(new And(new False(), new False()).apply(commodity1));
        assertFalse(new And(new False(), new True()) .apply(commodity1));
        assertFalse(new And(new True() , new False()).apply(commodity1));
    }

    @Test
    void OrTrue() {
        assertTrue(new Or(new True() , new True()) .apply(commodity1));
        assertTrue(new Or(new False(), new True()) .apply(commodity1));
        assertTrue(new Or(new True() , new False()).apply(commodity1));
    }

    @Test
    void OrFalse() {
        assertFalse(new Or(new False(), new False()).apply(commodity1));
    }

    @Test
    void ImpliesTrue() {
        assertTrue(new Implies(new False(), new False()).apply(commodity1));
        assertTrue(new Implies(new False(), new True()) .apply(commodity1));
        assertTrue(new Implies(new True() , new True()) .apply(commodity1));
    }

    @Test
    void ImpliesFalse() {
        assertFalse(new Implies(new True(), new False()).apply(commodity1));
    }

    @Test
    void QualityEqualsTrue() {
        assertTrue(new QualityEquals(1).apply(commodity1));
    }

    @Test
    void QualityEqualsFalse() {
        assertFalse(new QualityEquals(commodity2.getQuality()).apply(commodity1));
    }

    @Test
    void ExpirationDateEqualsTrue() {
        assertTrue(new ExpirationDateEquals(ExpirationDate.DoesNotExpire.instance()).apply(commodity1));
    }

    @Test
    void ExpirationDateEqualsFalse() {
        assertFalse(new ExpirationDateEquals(commodity2.expirationDate).apply(commodity1));
    }
}
