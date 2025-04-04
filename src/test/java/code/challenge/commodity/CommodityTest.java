package code.challenge.commodity;

import code.challenge.SimulationContext;
import code.challenge.currency.EUR;
import code.challenge.commodity.rule.*;
import code.challenge.commodity.rule.modular.SimpleRule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static code.challenge.Assertions.assertCloseTo;
public class CommodityTest {
    static Supplier<Optional<Commodity>> nonExpirableCommodity = () ->
            Commodity.of(
                    "Commodity",
                    new EUR(10),
                    42,
                    new GeneralRules()
            );
    static Supplier<Optional<Commodity>> expiredCommodity = () ->
            Commodity.of(
                    "Commodity",
                    new EUR(10),
                    42,
                    new ExpirationDate.ExpiresAt(LocalDate.now(SimulationContext.clock).minusDays(1)),
                    new GeneralRules()
            );
    static Function<Integer, Optional<Commodity>> expiresIn = expiresIn ->
            Commodity.of(
                    "Commodity",
                    new EUR(10),
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
        assertEquals(new EUR(10), commodity.basePrice);
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
        assertEquals(new EUR(10), commodity.basePrice);
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
        performDailyUpdateAndAdvanceClockBy(commodity, 1);

        assertEquals("Commodity", commodity.label);
        assertEquals(new EUR(10), commodity.basePrice);
        assertEquals(42, commodity.getQuality());
        assertEquals(ExpirationDate.DoesNotExpire.instance(), commodity.expirationDate);
    }

    @Test
    @DisplayName("expirable commodity using general rule set does not change on dailyUpdate")
    void generalExpirableCommodityDoesNotChange() {
        var maybeCommodity = expiredCommodity.get();

        assertTrue(maybeCommodity.isPresent());
        var commodity = maybeCommodity.get();
        performDailyUpdateAndAdvanceClockBy(commodity, 1);

        assertEquals("Commodity", commodity.label);
        assertEquals(new EUR(10), commodity.basePrice);
        assertEquals(42, commodity.getQuality());
        assertEquals(new ExpirationDate.ExpiresAt(LocalDate.now(SimulationContext.clock).minusDays(2)), commodity.expirationDate);
    }

    @Test
    @DisplayName("daily price of non expirable commodity using general rule set is determined by quality")
    void generalNonExpirableCommodityDailyPrice() {
        var maybeCommodity = nonExpirableCommodity.get();

        assertTrue(maybeCommodity.isPresent());
        var commodity = maybeCommodity.get();

        assertCloseTo(new EUR(14.2), commodity.dailyPrice(), 0.00001);
    }

    @Test
    @DisplayName("daily price of expirable commodity using general rule set is determined by quality")
    void generalExpirableCommodityDailyPrice() {
        var maybeCommodity = expiredCommodity.get();

        assertTrue(maybeCommodity.isPresent());
        var commodity = maybeCommodity.get();

        assertCloseTo(new EUR(14.2), commodity.dailyPrice(), 0.00001);
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
    @DisplayName("cheese should expire at some point")
    void cheeseShouldExpire() {
        assertTrue(
                Commodity.of(
                        "Cheese",
                        new EUR(1),
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
                        new EUR(1),
                        30,
                        new ExpirationDate.ExpiresAt(LocalDate.now(SimulationContext.clock).plusDays(49)),
                        new CheeseRules()
                ).isEmpty()
        );

        assertTrue(
                Commodity.of(
                        "Cheese",
                        new EUR(1),
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
                        new EUR(1),
                        30,
                        new ExpirationDate.ExpiresAt(LocalDate.now(SimulationContext.clock).plusDays(101)),
                        new CheeseRules()
                ).isEmpty()
        );

        assertTrue(
                Commodity.of(
                        "Cheese",
                        new EUR(1),
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
                        new EUR(1),
                        29,
                        new ExpirationDate.ExpiresAt(LocalDate.now(SimulationContext.clock).plusDays(75)),
                        new CheeseRules()
                ).isEmpty()
        );

        assertTrue(
                Commodity.of(
                        "Cheese",
                        new EUR(1),
                        30,
                        new ExpirationDate.ExpiresAt(LocalDate.now(SimulationContext.clock).plusDays(75)),
                        new CheeseRules()
                ).isPresent()
        );
    }

    @Test
    @DisplayName("commodity does not age on creation day")
    void noDailyUpdateOnCreationDay() {
        var maybeCommodity = Commodity.of(
                "Cheese",
                new EUR(1),
                31,
                new ExpirationDate.ExpiresAt(LocalDate.now(SimulationContext.clock).plusDays(75)),
                new CheeseRules()
        );

        assertTrue(maybeCommodity.isPresent());
        var commodity = maybeCommodity.get();
        commodity.dailyUpdate();

        assertEquals(31, commodity.getQuality());
    }

    @Test
    @DisplayName("commodity does not age twice on the same day")
    void onlyOneDailyUpdatePerDay() {
        var maybeCommodity = Commodity.of(
                "Cheese",
                new EUR(1),
                31,
                new ExpirationDate.ExpiresAt(LocalDate.now(SimulationContext.clock).plusDays(75)),
                new CheeseRules()
        );

        assertTrue(maybeCommodity.isPresent());
        var commodity = maybeCommodity.get();
        performDailyUpdateAndAdvanceClockBy(commodity, 1);
        commodity.dailyUpdate();

        assertEquals(30, commodity.getQuality());
    }

    @Test
    @DisplayName("daily price of cheese is determined by quality")
    void cheeseDailyPrice() {
        var maybeCommodity = Commodity.of(
                "Cheese",
                new EUR(1),
                30,
                new ExpirationDate.ExpiresAt(LocalDate.now(SimulationContext.clock).plusDays(75)),
                new CheeseRules()
        );

        assertTrue(maybeCommodity.isPresent());
        var commodity = maybeCommodity.get();

        assertCloseTo(new EUR(4), commodity.dailyPrice(), 0.00001);
    }

    @Test
    @DisplayName("cheese degrades in quality over time")
    void cheeseDegradesOnDailyUpdate() {
        var maybeCommodity = Commodity.of(
                "Cheese",
                new EUR(1),
                31,
                new ExpirationDate.ExpiresAt(LocalDate.now(SimulationContext.clock).plusDays(75)),
                new CheeseRules()
        );

        assertTrue(maybeCommodity.isPresent());
        var commodity = maybeCommodity.get();
        performDailyUpdateAndAdvanceClockBy(commodity, 1);

        assertEquals(30, commodity.getQuality());
    }

    @Test
    @DisplayName("cheese should be removed from the inventory if quality too low")
    void cheeseShouldBeRemoveOnLowQuality() {
        var maybeCommodity = Commodity.of(
                "Cheese",
                new EUR(1),
                30,
                new ExpirationDate.ExpiresAt(LocalDate.now(SimulationContext.clock).plusDays(75)),
                new CheeseRules()
        );

        assertTrue(maybeCommodity.isPresent());
        var commodity = maybeCommodity.get();
        performDailyUpdateAndAdvanceClockBy(commodity, 1);

        assertTrue(commodity.toRemove());
    }

    @Test
    @DisplayName("cheese should remain in the inventory if quality over threshold")
    void cheeseShouldRemainOnOkayQuality() {
        var maybeCommodity = Commodity.of(
                "Cheese",
                new EUR(1),
                30,
                new ExpirationDate.ExpiresAt(LocalDate.now(SimulationContext.clock).plusDays(75)),
                new CheeseRules()
        );

        assertTrue(maybeCommodity.isPresent());
        var commodity = maybeCommodity.get();

        assertFalse(commodity.toRemove());
    }

    @Test
    @DisplayName("wine should not expire")
    void wineShouldNotExpire() {
        assertTrue(
                Commodity.of(
                        "Wine",
                        new EUR(1),
                        30,
                        new ExpirationDate.ExpiresAt(LocalDate.now(SimulationContext.clock)),
                        new WineRules()
                ).isEmpty()
        );

        assertTrue(
                Commodity.of(
                        "Wine",
                        new EUR(1),
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
                        new EUR(1),
                        -1,
                        ExpirationDate.DoesNotExpire.instance(),
                        new WineRules()
                ).isEmpty()
        );

        assertTrue(
                Commodity.of(
                        "Wine",
                        new EUR(1),
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
                new EUR(1),
                0,
                ExpirationDate.DoesNotExpire.instance(),
                new WineRules()
        );

        assertTrue(maybeCommodity.isPresent());
        var commodity = maybeCommodity.get();

        IntStream.range(1, 10).forEach(dayOffset -> {
            performDailyUpdateAndAdvanceClockBy(commodity, dayOffset);
            assertEquals(0, commodity.getQuality());
        });

        performDailyUpdateAndAdvanceClockBy(commodity, 11);
        assertEquals(1, commodity.getQuality());
    }

    @Test
    @DisplayName("the daily price of wine is not determined by quality")
    void wineDailyPriceIsStatic() {
        var maybeCommodity = Commodity.of(
                "Wine",
                new EUR(1),
                10,
                ExpirationDate.DoesNotExpire.instance(),
                new WineRules()
        );

        assertTrue(maybeCommodity.isPresent());
        var commodity = maybeCommodity.get();

        assertCloseTo(new EUR(1), commodity.dailyPrice(), 0.0001);
    }

    @Test
    @DisplayName("bricks should not expire")
    void bricksShouldNotExpire() {
        assertTrue(
                Commodity.of(
                        "Bricks",
                        new EUR(1),
                        10,
                        new ExpirationDate.ExpiresAt(LocalDate.now(SimulationContext.clock)),
                        new BricksRules(LocalDate.now(SimulationContext.clock))
                ).isEmpty()
        );

        assertTrue(
                Commodity.of(
                        "Bricks",
                        new EUR(1),
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
                        new EUR(1),
                        9002,
                        ExpirationDate.DoesNotExpire.instance(),
                        new BricksRules(LocalDate.now(SimulationContext.clock))
                ).isEmpty()
        );

        assertTrue(
                Commodity.of(
                        "Bricks",
                        new EUR(1),
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
                new EUR(1),
                10,
                ExpirationDate.DoesNotExpire.instance(),
                new BricksRules(LocalDate.now(SimulationContext.clock).plusDays(1))
        );

        assertTrue(maybeCommodity.isPresent());
        var commodity = maybeCommodity.get();
        performDailyUpdateAndAdvanceClockBy(commodity, 1);

        assertEquals(10, commodity.getQuality());
    }

    @Test
    @DisplayName("bricks quality remains unchanged if production stopped less than a month ago")
    void bricksQualityRemainsIfNotYetOneMonthAfterEndOfProduction() {
        var maybeCommodity = Commodity.of(
                "Bricks",
                new EUR(1),
                10,
                ExpirationDate.DoesNotExpire.instance(),
                new BricksRules(LocalDate.now(SimulationContext.clock).minusMonths(1).plusDays(1))
        );

        assertTrue(maybeCommodity.isPresent());
        var commodity = maybeCommodity.get();
        performDailyUpdateAndAdvanceClockBy(commodity, 1);

        assertEquals(10, commodity.getQuality());
    }

    @Test
    @DisplayName("bricks quality increases moderately if one month after production end")
    void bricksQualityIncreasesIfOneMonthOverEndOfProduction() {
        var maybeCommodity = Commodity.of(
                "Bricks",
                new EUR(1),
                10,
                ExpirationDate.DoesNotExpire.instance(),
                new BricksRules(LocalDate.now(SimulationContext.clock).minusMonths(1).minusDays(1))
        );

        assertTrue(maybeCommodity.isPresent());
        var commodity = maybeCommodity.get();
        performDailyUpdateAndAdvanceClockBy(commodity, 1);

        assertEquals(11, commodity.getQuality());
    }

    @Test
    @DisplayName("bricks quality increases moderately until one year after production end")
    void bricksQualityIncreasesIfAtOneYearOverEndOfProduction() {
        var maybeCommodity = Commodity.of(
                "Bricks",
                new EUR(1),
                10,
                ExpirationDate.DoesNotExpire.instance(),
                new BricksRules(LocalDate.now(SimulationContext.clock).minusYears(1).plusDays(1))
        );

        assertTrue(maybeCommodity.isPresent());
        var commodity = maybeCommodity.get();
        performDailyUpdateAndAdvanceClockBy(commodity, 1);

        assertEquals(11, commodity.getQuality());
    }

    @Test
    @DisplayName("bricks quality increases largely if one year after production end")
    void bricksQualityIncreasesIfOneYearOverEndOfProduction() {
        var maybeCommodity = Commodity.of(
                "Bricks",
                new EUR(1),
                10,
                ExpirationDate.DoesNotExpire.instance(),
                new BricksRules(LocalDate.now(SimulationContext.clock).minusYears(1))
        );

        assertTrue(maybeCommodity.isPresent());
        var commodity = maybeCommodity.get();
        performDailyUpdateAndAdvanceClockBy(commodity, 1);

        assertEquals(20, commodity.getQuality());
    }

    @Test
    @DisplayName("commodity that doesn't age well decreases in quality over time")
    void degradingCommodityQualityReducesOnDailyUpdate() {
        var maybeCommodity = Commodity.of(
                "DegradingCommodity",
                new EUR(1),
                10,
                ExpirationDate.DoesNotExpire.instance(),
                new ModularRules(
                        new SimpleRule<>(_ -> false),
                        new SimpleRule<>(p -> p.basePrice),
                        p ->  p.setQuality(p.getQuality()-1)
                )
        );

        assertTrue(maybeCommodity.isPresent());
        var commodity = maybeCommodity.get();
        performDailyUpdateAndAdvanceClockBy(commodity, 1);

        assertEquals(9, commodity.getQuality());
    }

    @Test
    @DisplayName("resetting the quality to the same value does not notify the observers")
    void settingCommodityQualityToSameValue() {
        var maybeCommodity = Commodity.of(
                "DegradingCommodity",
                new EUR(1),
                10,
                ExpirationDate.DoesNotExpire.instance(),
                new GeneralRules()
        );

        assertTrue(maybeCommodity.isPresent());
        var commodity = maybeCommodity.get();
        commodity.setQuality(commodity.getQuality());

        assertEquals(10, commodity.getQuality());
    }

    private void performDailyUpdateAndAdvanceClockBy(Commodity commodity, int days) {
        SimulationContext.setClock(LocalDate.now(SimulationContext.clock).plusDays(days));
        commodity.dailyUpdate();
    }
}
