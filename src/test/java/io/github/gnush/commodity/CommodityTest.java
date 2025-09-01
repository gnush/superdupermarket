package io.github.gnush.commodity;

import io.github.gnush.SimulationContext;
import io.github.gnush.commodity.rule.*;
import io.github.gnush.commodity.rule.modular.lit.CommodityValue;
import io.github.gnush.commodity.rule.modular.lit.Literal;
import io.github.gnush.currency.EUR;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static io.github.gnush.Assertions.assertCloseTo;
import static io.github.gnush.commodity.CommodityTestUtil.performDailyUpdateAndAdvanceClockByOne;
public class CommodityTest {
    static Supplier<Optional<Commodity>> nonExpirableCommodity = () ->
            Commodity.of(
                    "Commodity",
                    new EUR(BigDecimal.TEN),
                    42,
                    new GeneralRules()
            );
    static Supplier<Optional<Commodity>> expiredCommodity = () ->
            Commodity.of(
                    "Commodity",
                    new EUR(BigDecimal.TEN),
                    42,
                    new ExpirationDate.ExpiresAt(LocalDate.now(SimulationContext.clock).minusDays(1)),
                    new GeneralRules()
            );
    static Function<Integer, Optional<Commodity>> expiresIn = expiresIn ->
            Commodity.of(
                    "Commodity",
                    new EUR(BigDecimal.TEN),
                    42,
                    new ExpirationDate.ExpiresAt(LocalDate.now(SimulationContext.clock).plusDays(expiresIn)),
                    new GeneralRules()
            );

    @Test
    @DisplayName("create non expirable commodity using general rule set")
    void generalNonExpirableCommodityCreation() {
        var maybeCommodity = nonExpirableCommodity.get();

        assertTrue(maybeCommodity.isPresent());
        var commodity = maybeCommodity.get();

        assertEquals("Commodity", commodity.label);
        assertEquals(new EUR(BigDecimal.TEN), commodity.basePrice);
        assertEquals(42, commodity.getQuality());
        assertEquals(ExpirationDate.DoesNotExpire.instance(), commodity.expirationDate);
    }

    @Test
    @DisplayName("create expirable commodity using general rule set")
    void generalExpirableCommodityCreation() {
        var maybeCommodity = expiredCommodity.get();

        assertTrue(maybeCommodity.isPresent());
        var commodity = maybeCommodity.get();

        assertEquals("Commodity", commodity.label);
        assertEquals(new EUR(BigDecimal.TEN), commodity.basePrice);
        assertEquals(42, commodity.getQuality());
        assertEquals(new ExpirationDate.ExpiresAt(LocalDate.now(SimulationContext.clock).minusDays(1)), commodity.expirationDate);
    }

    @Test
    @DisplayName("non-expirable commodity toString correct")
    void generalNonExpirableCommodityToString() {
        var maybeCommodity = nonExpirableCommodity.get();

        assertTrue(maybeCommodity.isPresent());
        var commodity = maybeCommodity.get();

        assertEquals(
                String.format(
                        "%s: basePrice='%s' quality=%s expiresOn=%s",
                        commodity.label, commodity.basePrice, commodity.getQuality(), commodity.expirationDate
                ),
                commodity.toString());
    }

    @Test
    @DisplayName("expirable commodity toString correct")
    void generalExpirableCommodityToString() {
        var maybeCommodity = expiresIn.apply(1);

        assertTrue(maybeCommodity.isPresent());
        var commodity = maybeCommodity.get();

        assertEquals(
                String.format(
                        "%s: basePrice='%s' quality=%s expiresOn=%s",
                        commodity.label, commodity.basePrice, commodity.getQuality(), commodity.expirationDate
                ),
                commodity.toString());
    }

    @Test
    @DisplayName("non-expirable commodity overview correct")
    void generalNonExpirableCommodityOverview() {
        var maybeCommodity = nonExpirableCommodity.get();

        assertTrue(maybeCommodity.isPresent());
        var commodity = maybeCommodity.get();

        assertEquals(
                String.format(
                        "%s: price='%s' quality=%s remove=%s",
                        commodity.label, commodity.dailyPrice(), commodity.getQuality(), commodity.toRemove()),
                commodity.overview());
    }

    @Test
    @DisplayName("non-expirable commodity pretty correct")
    void generalNonExpirableCommodityPretty() {
        var maybeCommodity = nonExpirableCommodity.get();

        assertTrue(maybeCommodity.isPresent());
        var commodity = maybeCommodity.get();

        assertEquals(
                String.format("%s (%s)\n%s", commodity.label, commodity.dailyPrice(), commodity.expirationDate),
                commodity.pretty()
        );
    }

    @Test
    @DisplayName("non-expirable commodity using general rule set should not be removed")
    void generalNonExpirableCommodityShouldNotBeRemoved(){
        var maybeCommodity = nonExpirableCommodity.get();

        assertTrue(maybeCommodity.isPresent());
        var commodity = maybeCommodity.get();

        assertFalse(commodity.toRemove());
    }

    @Test
    @DisplayName("non expirable commodity using general rule set does not change on dailyUpdate")
    void generalNonExpirableCommodityDoesNotChange() {
        var maybeCommodity = nonExpirableCommodity.get();

        assertTrue(maybeCommodity.isPresent());
        var commodity = maybeCommodity.get();
        performDailyUpdateAndAdvanceClockByOne(commodity);

        assertEquals("Commodity", commodity.label);
        assertEquals(new EUR(BigDecimal.TEN), commodity.basePrice);
        assertEquals(42, commodity.getQuality());
        assertEquals(ExpirationDate.DoesNotExpire.instance(), commodity.expirationDate);
    }

    @Test
    @DisplayName("expirable commodity using general rule set does not change on dailyUpdate")
    void generalExpirableCommodityDoesNotChange() {
        var maybeCommodity = expiredCommodity.get();

        assertTrue(maybeCommodity.isPresent());
        var commodity = maybeCommodity.get();
        performDailyUpdateAndAdvanceClockByOne(commodity);

        assertEquals("Commodity", commodity.label);
        assertEquals(new EUR(BigDecimal.TEN), commodity.basePrice);
        assertEquals(42, commodity.getQuality());
        assertEquals(new ExpirationDate.ExpiresAt(LocalDate.now(SimulationContext.clock).minusDays(2)), commodity.expirationDate);
    }

    @Test
    @DisplayName("daily price of non expirable commodity using general rule set is determined by quality")
    void generalNonExpirableCommodityDailyPrice() {
        var maybeCommodity = nonExpirableCommodity.get();

        assertTrue(maybeCommodity.isPresent());
        var commodity = maybeCommodity.get();

        assertCloseTo(new EUR(new BigDecimal("14.2")), commodity.dailyPrice(), 0.00001);
    }

    @Test
    @DisplayName("daily price of expirable commodity using general rule set is determined by quality")
    void generalExpirableCommodityDailyPrice() {
        var maybeCommodity = expiredCommodity.get();

        assertTrue(maybeCommodity.isPresent());
        var commodity = maybeCommodity.get();

        assertCloseTo(new EUR(new BigDecimal("14.2")), commodity.dailyPrice(), 0.00001);
    }

    @Test
    @DisplayName("expired commodity should be removed from the inventory")
    void expiredCommodityShouldBeRemoved() {
        var maybeCommodity = expiredCommodity.get();

        assertTrue(maybeCommodity.isPresent());
        var commodity = maybeCommodity.get();

        assertTrue(commodity.toRemove());
    }

    @Test
    @DisplayName("non expired commodity should remain in the inventory")
    void nonExpiredCommodityShouldRemain() {
        var maybeCommodity = expiresIn.apply(1);

        assertTrue(maybeCommodity.isPresent());
        var commodity = maybeCommodity.get();

        assertFalse(commodity.toRemove());
    }

    @Test
    @DisplayName("commodity does not age on creation day")
    void noDailyUpdateOnCreationDay() {
        var maybeCommodity = Commodity.of(
                "Commodity",
                new EUR(BigDecimal.ONE),
                31,
                new ExpirationDate.ExpiresAt(LocalDate.now(SimulationContext.clock).plusDays(75)),
                new ModularRules(
                        Literal.of(false),
                        CommodityValue.basePrice(),
                        c -> c.setQuality(c.getQuality()+1)
                )
        );

        assertTrue(maybeCommodity.isPresent());
        var commodity = maybeCommodity.get();
        commodity.dailyUpdate();

        assertEquals(31, commodity.getQuality());
    }

    @Test
    @DisplayName("commodity that doesn't age well decreases in quality over time")
    void degradingCommodityQualityReducesOnDailyUpdate() {
        var maybeCommodity = Commodity.of(
                "DegradingCommodity",
                new EUR(BigDecimal.ONE),
                10,
                ExpirationDate.DoesNotExpire.instance(),
                new ModularRules(
                        Literal.of(false),
                        CommodityValue.basePrice(),
                        c ->  c.setQuality(c.getQuality()-1)
                )
        );

        assertTrue(maybeCommodity.isPresent());
        var commodity = maybeCommodity.get();
        performDailyUpdateAndAdvanceClockByOne(commodity);

        assertEquals(9, commodity.getQuality());
    }

    @Test
    @DisplayName("commodity does not age twice on the same day")
    void onlyOneDailyUpdatePerDay() {
        var maybeCommodity = Commodity.of(
                "Commodity",
                new EUR(BigDecimal.ONE),
                31,
                new ExpirationDate.ExpiresAt(LocalDate.now(SimulationContext.clock).plusDays(75)),
                new ModularRules(
                        Literal.of(false),
                        CommodityValue.basePrice(),
                        c -> c.setQuality(c.getQuality()-1)
                )
        );

        assertTrue(maybeCommodity.isPresent());
        var commodity = maybeCommodity.get();
        performDailyUpdateAndAdvanceClockByOne(commodity);
        commodity.dailyUpdate();

        assertEquals(30, commodity.getQuality());
    }

    @Test
    @DisplayName("resetting the quality to the same value does not notify the observers")
    void settingCommodityQualityToSameValue() {
        var maybeCommodity = Commodity.of(
                "DegradingCommodity",
                new EUR(BigDecimal.ONE),
                10,
                ExpirationDate.DoesNotExpire.instance(),
                new GeneralRules()
        );

        assertTrue(maybeCommodity.isPresent());
        var commodity = maybeCommodity.get();
        commodity.setQuality(commodity.getQuality());

        assertEquals(10, commodity.getQuality());
    }
}
