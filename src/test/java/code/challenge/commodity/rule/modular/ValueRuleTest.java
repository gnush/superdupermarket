package code.challenge.commodity.rule.modular;

import code.challenge.commodity.Commodity;
import code.challenge.commodity.ExpirationDate;
import code.challenge.commodity.rule.GeneralRules;
import code.challenge.currency.EUR;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static code.challenge.commodity.rule.modular.lit.CommodityValue.*;
public class ValueRuleTest {
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
                "perishable",
                new EUR(1),
                -2,
                new ExpirationDate.ExpiresAt(LocalDate.of(1900, 1, 1)),
                new GeneralRules()
        ).orElseThrow(
                () -> new AssertionFailedError("@Before commodity2 creation failed")
        );
    }

    @Test
    void accessCommodityLabelRuleStyle() {
        assertEquals(
                "Test",
                label().apply(commodity1)
        );
        assertEquals(
                "perishable",
                label().apply(commodity2)
        );
    }

    @Test
    void accessCommodityQualityRuleStyle() {
        assertEquals(
                1,
                quality().apply(commodity1)
        );
        assertEquals(
                -2,
                quality().apply(commodity2)
        );
    }

    @Test
    void accessCommodityExpirationDateRuleStyle() {
        assertEquals(
                ExpirationDate.DoesNotExpire.instance(),
                expirationDate().apply(commodity1)
        );
        assertEquals(
                new ExpirationDate.ExpiresAt(LocalDate.of(1900, 1, 1)),
                expirationDate().apply(commodity2)
        );
    }
}
