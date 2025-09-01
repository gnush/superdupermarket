package io.github.gnush.commodity.rule;

import io.github.gnush.SimulationContext;
import io.github.gnush.commodity.Commodity;
import io.github.gnush.commodity.ExpirationDate;
import io.github.gnush.currency.EUR;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.IntStream;

import static io.github.gnush.Assertions.assertCloseTo;
import static io.github.gnush.commodity.CommodityTestUtil.performDailyUpdateAndAdvanceClockByOne;
import static org.junit.jupiter.api.Assertions.*;

public class CheeseTest {
    @Test
    @DisplayName("cheese should expire at some point")
    void cheeseShouldExpire() {
        Assertions.assertTrue(
                Commodity.of(
                        "Cheese",
                        new EUR(BigDecimal.ONE),
                        30,
                        ExpirationDate.DoesNotExpire.instance(),
                        new CheeseRules()
                ).isEmpty()
        );
    }

    @Test
    @DisplayName("cheese should not expire for 50 days")
    void cheeseCreationFailsIfExpirationDateTooSoon() {
        assertTrue(
                Commodity.of(
                        "Cheese",
                        new EUR(BigDecimal.ONE),
                        30,
                        new ExpirationDate.ExpiresAt(LocalDate.now(SimulationContext.clock).plusDays(49)),
                        new CheeseRules()
                ).isEmpty()
        );

        assertTrue(
                Commodity.of(
                        "Cheese",
                        new EUR(BigDecimal.ONE),
                        30,
                        new ExpirationDate.ExpiresAt(LocalDate.now(SimulationContext.clock).plusDays(50)),
                        new CheeseRules()
                ).isPresent()
        );
    }

    @Test
    @DisplayName("cheese should expire before 100 days")
    void cheeseCreationFailsIfExpirationDateTooFarAway() {
        assertTrue(
                Commodity.of(
                        "Cheese",
                        new EUR(BigDecimal.ONE),
                        30,
                        new ExpirationDate.ExpiresAt(LocalDate.now(SimulationContext.clock).plusDays(101)),
                        new CheeseRules()
                ).isEmpty()
        );

        assertTrue(
                Commodity.of(
                        "Cheese",
                        new EUR(BigDecimal.ONE),
                        30,
                        new ExpirationDate.ExpiresAt(LocalDate.now(SimulationContext.clock).plusDays(100)),
                        new CheeseRules()
                ).isPresent()
        );
    }

    @Test
    @DisplayName("cheese should have at least a quality of 30")
    void cheeseCreationFailsIfQualityTooLow() {
        assertTrue(
                Commodity.of(
                        "Cheese",
                        new EUR(BigDecimal.ONE),
                        29,
                        new ExpirationDate.ExpiresAt(LocalDate.now(SimulationContext.clock).plusDays(75)),
                        new CheeseRules()
                ).isEmpty()
        );

        assertTrue(
                Commodity.of(
                        "Cheese",
                        new EUR(BigDecimal.ONE),
                        30,
                        new ExpirationDate.ExpiresAt(LocalDate.now(SimulationContext.clock).plusDays(75)),
                        new CheeseRules()
                ).isPresent()
        );
    }

    @Test
    @DisplayName("daily price of cheese is determined by quality")
    void cheeseDailyPrice() {
        var maybeCommodity = Commodity.of(
                "Cheese",
                new EUR(BigDecimal.ONE),
                30,
                new ExpirationDate.ExpiresAt(LocalDate.now(SimulationContext.clock).plusDays(75)),
                new CheeseRules()
        );

        assertTrue(maybeCommodity.isPresent());
        var commodity = maybeCommodity.get();

        assertCloseTo(new EUR(new BigDecimal("4")), commodity.dailyPrice(), 0.00001);
    }

    @Test
    @DisplayName("cheese degrades in quality over time")
    void cheeseDegradesOnDailyUpdate() {
        var maybeCommodity = Commodity.of(
                "Cheese",
                new EUR(BigDecimal.ONE),
                31,
                new ExpirationDate.ExpiresAt(LocalDate.now(SimulationContext.clock).plusDays(75)),
                new CheeseRules()
        );

        assertTrue(maybeCommodity.isPresent());
        var commodity = maybeCommodity.get();
        performDailyUpdateAndAdvanceClockByOne(commodity);

        assertEquals(30, commodity.getQuality());
    }

    @Test
    @DisplayName("cheese should be removed from the inventory if past it's expiration date")
    void cheeseShouldBeRemovedIfExpired() {
        var maybeCommodity = Commodity.of(
                "Cheese",
                new EUR(BigDecimal.ONE),
                1000,
                new ExpirationDate.ExpiresAt(LocalDate.now(SimulationContext.clock).plusDays(50)),
                new CheeseRules()
        );

        assertTrue(maybeCommodity.isPresent());
        var commodity = maybeCommodity.get();

        IntStream.range(1, 51).forEach(_ -> {
            performDailyUpdateAndAdvanceClockByOne(commodity);
            assertFalse(commodity.toRemove());
        });

        performDailyUpdateAndAdvanceClockByOne(commodity);
        assertTrue(commodity.toRemove());
    }

    @Test
    @DisplayName("cheese should be removed from the inventory if quality too low")
    void cheeseShouldBeRemovedOnLowQuality() {
        var maybeCommodity = Commodity.of(
                "Cheese",
                new EUR(BigDecimal.ONE),
                30,
                new ExpirationDate.ExpiresAt(LocalDate.now(SimulationContext.clock).plusDays(75)),
                new CheeseRules()
        );

        assertTrue(maybeCommodity.isPresent());
        var commodity = maybeCommodity.get();

        assertFalse(commodity.toRemove());
        performDailyUpdateAndAdvanceClockByOne(commodity);
        assertTrue(commodity.toRemove());
    }

    @Test
    @DisplayName("cheese should remain in the inventory if quality over threshold")
    void cheeseShouldRemainOnOkayQuality() {
        var maybeCommodity = Commodity.of(
                "Cheese",
                new EUR(BigDecimal.ONE),
                30,
                new ExpirationDate.ExpiresAt(LocalDate.now(SimulationContext.clock).plusDays(75)),
                new CheeseRules()
        );

        assertTrue(maybeCommodity.isPresent());
        var commodity = maybeCommodity.get();

        assertFalse(commodity.toRemove());
    }
}
