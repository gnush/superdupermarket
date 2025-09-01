package io.github.gnush.commodity.rule.modular;

import io.github.gnush.commodity.Commodity;
import io.github.gnush.commodity.rule.GeneralRules;
import io.github.gnush.commodity.rule.modular.lit.Literal;
import io.github.gnush.commodity.rule.modular.update.If;
import io.github.gnush.commodity.rule.modular.update.Noop;
import io.github.gnush.commodity.rule.modular.update.SetQuality;
import io.github.gnush.currency.EUR;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
public class UpdateRuleTest {
    Commodity commodity1 = null;

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
    }

    @Test
    void noopDoesNotChangeQuality() {
        new Noop().apply(commodity1);

        assertEquals(1, commodity1.getQuality());
    }

    @Test
    void setQualitySetsTheQuality() {
        new SetQuality(7).apply(commodity1);

        assertEquals(7, commodity1.getQuality());
    }

    @Test
    void ifTrueExecutesThen() {
        new If(
                Literal.of(true),
                new SetQuality(7),
                new SetQuality(-7)
        ).apply(commodity1);

        assertEquals(7, commodity1.getQuality());
    }

    @Test
    void ifFalseExecutesElse() {
        new If(
                Literal.of(false),
                new SetQuality(7),
                new SetQuality(-7)
        ).apply(commodity1);

        assertEquals(-7, commodity1.getQuality());
    }
}
