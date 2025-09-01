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

public class WineTest {
    @Test
    @DisplayName("wine should not expire")
    void wineShouldNotExpire() {
        Assertions.assertTrue(
                Commodity.of(
                        "Wine",
                        new EUR(BigDecimal.ONE),
                        30,
                        new ExpirationDate.ExpiresAt(LocalDate.now(SimulationContext.clock)),
                        new WineRules()
                ).isEmpty()
        );

        assertTrue(
                Commodity.of(
                        "Wine",
                        new EUR(BigDecimal.ONE),
                        30,
                        ExpirationDate.DoesNotExpire.instance(),
                        new WineRules()
                ).isPresent()
        );
    }

    @Test
    @DisplayName("wine quality should be non negative")
    void wineQualityShouldBeNonNegative() {
        assertTrue(
                Commodity.of(
                        "Wine",
                        new EUR(BigDecimal.ONE),
                        -1,
                        ExpirationDate.DoesNotExpire.instance(),
                        new WineRules()
                ).isEmpty()
        );

        assertTrue(
                Commodity.of(
                        "Wine",
                        new EUR(BigDecimal.ONE),
                        0,
                        ExpirationDate.DoesNotExpire.instance(),
                        new WineRules()
                ).isPresent()
        );
    }

    @Test
    @DisplayName("wine increases in quality after 10 days")
    void wineQualityIncreasesOverTime() {
        var maybeCommodity = Commodity.of(
                "Wine",
                new EUR(BigDecimal.ONE),
                0,
                ExpirationDate.DoesNotExpire.instance(),
                new WineRules()
        );

        assertTrue(maybeCommodity.isPresent());
        var commodity = maybeCommodity.get();

        IntStream.range(1, 10).forEach(_ -> {
            performDailyUpdateAndAdvanceClockByOne(commodity);
            assertEquals(0, commodity.getQuality());
        });

        performDailyUpdateAndAdvanceClockByOne(commodity);
        assertEquals(1, commodity.getQuality());
    }

    @Test
    @DisplayName("wine quality does not increase beyond 50")
    void wineQualityDoesNotIncreasesBeyond50() {
        var maybeCommodity = Commodity.of(
                "Wine",
                new EUR(BigDecimal.ONE),
                50,
                ExpirationDate.DoesNotExpire.instance(),
                new WineRules()
        );

        assertTrue(maybeCommodity.isPresent());
        var commodity = maybeCommodity.get();

        IntStream.range(1, 11).forEach(_ -> {
            performDailyUpdateAndAdvanceClockByOne(commodity);
            assertEquals(50, commodity.getQuality());
        });
    }

    @Test
    @DisplayName("the daily price of wine is not determined by quality")
    void wineDailyPriceIsStatic() {
        var maybeCommodity = Commodity.of(
                "Wine",
                new EUR(BigDecimal.ONE),
                10,
                ExpirationDate.DoesNotExpire.instance(),
                new WineRules()
        );

        assertTrue(maybeCommodity.isPresent());
        var commodity = maybeCommodity.get();

        assertCloseTo(new EUR(BigDecimal.ONE), commodity.dailyPrice(), 0.0001);
    }

    @Test
    @DisplayName("wine should not be removed")
    void wineShouldNotBeRemoved(){
        var maybeCommodity = Commodity.of(
                "Wine",
                new EUR(BigDecimal.ONE),
                10,
                ExpirationDate.DoesNotExpire.instance(),
                new WineRules()
        );

        assertTrue(maybeCommodity.isPresent());
        var commodity = maybeCommodity.get();

        assertFalse(commodity.toRemove());
    }
}
