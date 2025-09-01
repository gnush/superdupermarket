package io.github.gnush.commodity.rule;

import io.github.gnush.commodity.Commodity;
import io.github.gnush.commodity.rule.modular.SimpleRule;
import io.github.gnush.commodity.rule.modular.lit.Literal;
import io.github.gnush.currency.EUR;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static io.github.gnush.commodity.CommodityTestUtil.performDailyUpdateAndAdvanceClockByOne;
import static org.junit.jupiter.api.Assertions.*;

public class ModularTest {
    private final ModularRules decreasingQualityRules = new ModularRules(
            Literal.of(false),
            new SimpleRule<>(c -> c.basePrice.add(BigDecimal.valueOf(-c.getQuality()))),
            c -> c.setQuality(c.getQuality()-1)
    );

    @Test
    @DisplayName("commodity that doesn't age well decreases in quality over time")
    void degradingCommodityQualityReducesOnDailyUpdate() {
        var maybeCommodity = Commodity.of(
                "DegradingCommodity",
                new EUR(BigDecimal.ONE),
                10,
                decreasingQualityRules
        );

        assertTrue(maybeCommodity.isPresent());
        var commodity = maybeCommodity.get();
        performDailyUpdateAndAdvanceClockByOne(commodity);

        assertEquals(9, commodity.getQuality());
    }

    @Test
    @DisplayName("commodity that doesn't age well should not be removed")
    void degradingCommodityShouldNotBeRemoved() {
        var maybeCommodity = Commodity.of(
                "DegradingCommodity",
                new EUR(BigDecimal.ONE),
                10,
                decreasingQualityRules
        );

        assertTrue(maybeCommodity.isPresent());
        var commodity = maybeCommodity.get();

        assertFalse(commodity.toRemove());
    }

    @Test
    @DisplayName("daily price of commodity that doesn't age well is determined by quality")
    void degradingCommodityDailyPrice() {
        var maybeCommodity = Commodity.of(
                "DegradingCommodity",
                new EUR(BigDecimal.TEN),
                9,
                decreasingQualityRules
        );

        assertTrue(maybeCommodity.isPresent());
        var commodity = maybeCommodity.get();

        assertEquals(new EUR(BigDecimal.ONE), commodity.dailyPrice());
    }
}
