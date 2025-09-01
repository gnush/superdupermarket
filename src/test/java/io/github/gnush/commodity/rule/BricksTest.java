package io.github.gnush.commodity.rule;

import io.github.gnush.SimulationContext;
import io.github.gnush.commodity.Commodity;
import io.github.gnush.commodity.ExpirationDate;
import io.github.gnush.currency.EUR;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static io.github.gnush.commodity.CommodityTestUtil.performDailyUpdateAndAdvanceClockByOne;
import static org.junit.jupiter.api.Assertions.*;

public class BricksTest {
    @Test
    @DisplayName("parsing bricks rules fails when not supplied with a date")
    void parseBricksFails() {
        assertTrue(
                BricksRules.parse(Collections.emptyList()).isEmpty()
        );
    }

    @Test
    @DisplayName("parsing bricks rules succeeds when supplied with a date")
    void parseBricksSucceeds() {
        assertTrue(
                BricksRules.parse(List.of(SimulationContext.now().toString())).isPresent()
        );
    }

    @Test
    @DisplayName("bricks should not expire")
    void bricksShouldNotExpire() {
        assertTrue(
                Commodity.of(
                        "Bricks",
                        new EUR(BigDecimal.ONE),
                        10,
                        new ExpirationDate.ExpiresAt(LocalDate.now(SimulationContext.clock)),
                        new BricksRules(LocalDate.now(SimulationContext.clock))
                ).isEmpty()
        );

        assertTrue(
                Commodity.of(
                        "Bricks",
                        new EUR(BigDecimal.ONE),
                        10,
                        ExpirationDate.DoesNotExpire.instance(),
                        new BricksRules(LocalDate.now(SimulationContext.clock))
                ).isPresent()
        );
    }

    @Test
    @DisplayName("bricks quality should not exceed 9002")
    void bricksCreationFailsIfOverQualityThreshold() {
        assertTrue(
                Commodity.of(
                        "Bricks",
                        new EUR(BigDecimal.ONE),
                        9002,
                        ExpirationDate.DoesNotExpire.instance(),
                        new BricksRules(LocalDate.now(SimulationContext.clock))
                ).isEmpty()
        );

        assertTrue(
                Commodity.of(
                        "Bricks",
                        new EUR(BigDecimal.ONE),
                        9001,
                        ExpirationDate.DoesNotExpire.instance(),
                        new BricksRules(LocalDate.now(SimulationContext.clock))
                ).isPresent()
        );
    }

    @Test
    @DisplayName("bricks quality remains unchanged if commodity is still produced")
    void bricksQualityRemainsIfStillProduced() {
        var maybeCommodity = Commodity.of(
                "Bricks",
                new EUR(BigDecimal.ONE),
                10,
                ExpirationDate.DoesNotExpire.instance(),
                new BricksRules(LocalDate.now(SimulationContext.clock).plusDays(1))
        );

        assertTrue(maybeCommodity.isPresent());
        var commodity = maybeCommodity.get();
        performDailyUpdateAndAdvanceClockByOne(commodity);

        assertEquals(10, commodity.getQuality());
    }

    @Test
    @DisplayName("bricks quality remains unchanged if production stopped less than a month ago")
    void bricksQualityRemainsIfNotYetOneMonthAfterEndOfProduction() {
        var maybeCommodity = Commodity.of(
                "Bricks",
                new EUR(BigDecimal.ONE),
                10,
                ExpirationDate.DoesNotExpire.instance(),
                new BricksRules(LocalDate.now(SimulationContext.clock).minusMonths(1).plusDays(1))
        );

        assertTrue(maybeCommodity.isPresent());
        var commodity = maybeCommodity.get();
        performDailyUpdateAndAdvanceClockByOne(commodity);

        assertEquals(10, commodity.getQuality());
    }

    @Test
    @DisplayName("bricks quality increases moderately if one month after production end")
    void bricksQualityIncreasesIfOneMonthOverEndOfProduction() {
        var maybeCommodity = Commodity.of(
                "Bricks",
                new EUR(BigDecimal.ONE),
                10,
                ExpirationDate.DoesNotExpire.instance(),
                new BricksRules(LocalDate.now(SimulationContext.clock).minusMonths(1).minusDays(1))
        );

        assertTrue(maybeCommodity.isPresent());
        var commodity = maybeCommodity.get();
        performDailyUpdateAndAdvanceClockByOne(commodity);

        assertEquals(11, commodity.getQuality());
    }

    @Test
    @DisplayName("bricks quality increases moderately until one year after production end")
    void bricksQualityIncreasesIfAtOneYearOverEndOfProduction() {
        var maybeCommodity = Commodity.of(
                "Bricks",
                new EUR(BigDecimal.ONE),
                10,
                ExpirationDate.DoesNotExpire.instance(),
                new BricksRules(LocalDate.now(SimulationContext.clock).minusYears(1).plusDays(1))
        );

        assertTrue(maybeCommodity.isPresent());
        var commodity = maybeCommodity.get();
        performDailyUpdateAndAdvanceClockByOne(commodity);

        assertEquals(11, commodity.getQuality());
    }

    @Test
    @DisplayName("bricks quality increases largely if one year after production end")
    void bricksQualityIncreasesIfOneYearOverEndOfProduction() {
        var maybeCommodity = Commodity.of(
                "Bricks",
                new EUR(BigDecimal.ONE),
                10,
                ExpirationDate.DoesNotExpire.instance(),
                new BricksRules(LocalDate.now(SimulationContext.clock).minusYears(1))
        );

        assertTrue(maybeCommodity.isPresent());
        var commodity = maybeCommodity.get();
        performDailyUpdateAndAdvanceClockByOne(commodity);

        assertEquals(20, commodity.getQuality());
    }

    @Test
    @DisplayName("bricks should not be removed")
    void bricksShouldNotBeRemoved() {
        var maybeCommodity = Commodity.of(
                "Bricks",
                new EUR(BigDecimal.ONE),
                10,
                ExpirationDate.DoesNotExpire.instance(),
                new BricksRules(LocalDate.now(SimulationContext.clock).minusYears(1))
        );

        assertTrue(maybeCommodity.isPresent());
        var commodity = maybeCommodity.get();

        assertFalse(commodity.toRemove());
    }
}
