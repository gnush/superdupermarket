package code.challenge.product;

import code.challenge.currency.EUR;
import code.challenge.product.rule.BricksRules;
import code.challenge.product.rule.CheeseRules;
import code.challenge.product.rule.GeneralRules;
import code.challenge.product.rule.WineRules;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static code.challenge.Assertions.assertCloseTo;
public class ProductTest {
    static Supplier<Optional<Product>> nonExpirableProduct = () ->
            Product.of(
                    "Product",
                    new EUR(10),
                    42,
                    new GeneralRules()
            );
    static Supplier<Optional<Product>> expiredProduct = () ->
            Product.of(
                    "Product",
                    new EUR(10),
                    42,
                    new ExpirationDate.ExpiresAt(LocalDate.of(2000, 1, 1)),
                    new GeneralRules()
            );
    static Function<Integer, Optional<Product>> expiresIn = expiresIn ->
            Product.of(
                    "Product",
                    new EUR(10),
                    42,
                    new ExpirationDate.ExpiresAt(LocalDate.now().plusDays(expiresIn)),
                    new GeneralRules()
            );

    @Test
    @DisplayName("create non expirable product using general rule set")
    void generalNonExpirableProductCreation() {
        var maybeProduct = nonExpirableProduct.get();

        assertTrue(maybeProduct.isPresent());
        var product = maybeProduct.get();

        assertEquals("Product", product.label);
        assertEquals(new EUR(10), product.basePrice);
        assertEquals(42, product.getQuality());
        assertEquals(ExpirationDate.DoesNotExpire.instance(), product.expirationDate);
    }

    @Test
    @DisplayName("create expirable product using general rule set")
    void generalExpirableProductCreation() {
        var maybeProduct = expiredProduct.get();

        assertTrue(maybeProduct.isPresent());
        var product = maybeProduct.get();

        assertEquals("Product", product.label);
        assertEquals(new EUR(10), product.basePrice);
        assertEquals(42, product.getQuality());
        assertEquals(new ExpirationDate.ExpiresAt(LocalDate.of(2000, 1, 1)), product.expirationDate);
    }

    @Test
    @DisplayName("non expirable product using general rule set does not change on dailyUpdate")
    void generalNonExpirableProductDoesNotChange() {
        var maybeProduct = nonExpirableProduct.get();

        assertTrue(maybeProduct.isPresent());
        var product = maybeProduct.get();
        product.dailyUpdate();

        assertEquals("Product", product.label);
        assertEquals(new EUR(10), product.basePrice);
        assertEquals(42, product.getQuality());
        assertEquals(ExpirationDate.DoesNotExpire.instance(), product.expirationDate);
    }

    @Test
    @DisplayName("expirable product using general rule set does not change on dailyUpdate")
    void generalExpirableProductDoesNotChange() {
        var maybeProduct = expiredProduct.get();

        assertTrue(maybeProduct.isPresent());
        var product = maybeProduct.get();
        product.dailyUpdate();

        assertEquals("Product", product.label);
        assertEquals(new EUR(10), product.basePrice);
        assertEquals(42, product.getQuality());
        assertEquals(new ExpirationDate.ExpiresAt(LocalDate.of(2000, 1, 1)), product.expirationDate);
    }

    @Test
    @DisplayName("daily price of non expirable product using general rule set is determined by quality")
    void generalNonExpirableProductDailyPrice() {
        var maybeProduct = nonExpirableProduct.get();

        assertTrue(maybeProduct.isPresent());
        var product = maybeProduct.get();

        assertCloseTo(new EUR(14.2), product.dailyPrice(), 0.00001);
    }

    @Test
    @DisplayName("daily price of expirable product using general rule set is determined by quality")
    void generalExpirableProductDailyPrice() {
        var maybeProduct = expiredProduct.get();

        assertTrue(maybeProduct.isPresent());
        var product = maybeProduct.get();

        assertCloseTo(new EUR(14.2), product.dailyPrice(), 0.00001);
    }

    @Test
    @DisplayName("expired product should be removed from the inventory")
    void expiredProductShouldBeRemoved() {
        var maybeProduct = expiredProduct.get();

        assertTrue(maybeProduct.isPresent());
        var product = maybeProduct.get();

        assertTrue(product.toRemove());
    }

    @Test
    @DisplayName("non expired product should remain in the inventory")
    void nonExpiredProductShouldRemain() {
        var maybeProduct = expiresIn.apply(1);

        assertTrue(maybeProduct.isPresent());
        var product = maybeProduct.get();

        assertFalse(product.toRemove());
    }

    @Test
    @DisplayName("cheese should expire at some point")
    void cheeseShouldExpire() {
        assertTrue(
                Product.of(
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
                Product.of(
                        "Cheese",
                        new EUR(1),
                        30,
                        new ExpirationDate.ExpiresAt(LocalDate.now().plusDays(49)),
                        new CheeseRules()
                ).isEmpty()
        );

        assertTrue(
                Product.of(
                        "Cheese",
                        new EUR(1),
                        30,
                        new ExpirationDate.ExpiresAt(LocalDate.now().plusDays(50)),
                        new CheeseRules()
                ).isPresent()
        );
    }

    @Test
    @DisplayName("cheese should expire before 100 days")
    void cheeseCreationFailsIfExpirationDateTooFarAway() {
        assertTrue(
                Product.of(
                        "Cheese",
                        new EUR(1),
                        30,
                        new ExpirationDate.ExpiresAt(LocalDate.now().plusDays(101)),
                        new CheeseRules()
                ).isEmpty()
        );

        assertTrue(
                Product.of(
                        "Cheese",
                        new EUR(1),
                        30,
                        new ExpirationDate.ExpiresAt(LocalDate.now().plusDays(100)),
                        new CheeseRules()
                ).isPresent()
        );
    }

    @Test
    @DisplayName("cheese should have at least a quality of 30")
    void cheeseCreationFailsIfQualityTooLow() {
        assertTrue(
                Product.of(
                        "Cheese",
                        new EUR(1),
                        29,
                        new ExpirationDate.ExpiresAt(LocalDate.now().plusDays(75)),
                        new CheeseRules()
                ).isEmpty()
        );

        assertTrue(
                Product.of(
                        "Cheese",
                        new EUR(1),
                        30,
                        new ExpirationDate.ExpiresAt(LocalDate.now().plusDays(75)),
                        new CheeseRules()
                ).isPresent()
        );
    }

    @Test
    @DisplayName("daily price of cheese is determined by quality")
    void cheeseDailyPrice() {
        var maybeProduct = Product.of(
                "Cheese",
                new EUR(1),
                30,
                new ExpirationDate.ExpiresAt(LocalDate.now().plusDays(75)),
                new CheeseRules()
        );

        assertTrue(maybeProduct.isPresent());
        var product = maybeProduct.get();

        assertCloseTo(new EUR(4), product.dailyPrice(), 0.00001);
    }

    @Test
    @DisplayName("cheese degrades in quality over time")
    void cheeseDegradesOnDailyUpdate() {
        var maybeProduct = Product.of(
                "Cheese",
                new EUR(1),
                31,
                new ExpirationDate.ExpiresAt(LocalDate.now().plusDays(75)),
                new CheeseRules()
        );

        assertTrue(maybeProduct.isPresent());
        var product = maybeProduct.get();
        product.dailyUpdate();

        assertEquals(30, product.getQuality());
    }

    @Test
    @DisplayName("cheese should be removed from the inventory if quality too low")
    void cheeseShouldBeRemoveOnLowQuality() {
        var maybeProduct = Product.of(
                "Cheese",
                new EUR(1),
                30,
                new ExpirationDate.ExpiresAt(LocalDate.now().plusDays(75)),
                new CheeseRules()
        );

        assertTrue(maybeProduct.isPresent());
        var product = maybeProduct.get();
        product.dailyUpdate();

        assertTrue(product.toRemove());
    }

    @Test
    @DisplayName("cheese should remain in the inventory if quality over threshold")
    void cheeseShouldRemainOnOkayQuality() {
        var maybeProduct = Product.of(
                "Cheese",
                new EUR(1),
                30,
                new ExpirationDate.ExpiresAt(LocalDate.now().plusDays(75)),
                new CheeseRules()
        );

        assertTrue(maybeProduct.isPresent());
        var product = maybeProduct.get();

        assertFalse(product.toRemove());
    }

    @Test
    @DisplayName("wine should not expire")
    void wineShouldNotExpire() {
        assertTrue(
                Product.of(
                        "Wine",
                        new EUR(1),
                        30,
                        new ExpirationDate.ExpiresAt(LocalDate.now()),
                        new WineRules()
                ).isEmpty()
        );

        assertTrue(
                Product.of(
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
                Product.of(
                        "Wine",
                        new EUR(1),
                        -1,
                        ExpirationDate.DoesNotExpire.instance(),
                        new WineRules()
                ).isEmpty()
        );

        assertTrue(
                Product.of(
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
        var maybeProduct = Product.of(
                "Wine",
                new EUR(1),
                0,
                ExpirationDate.DoesNotExpire.instance(),
                new WineRules()
        );

        assertTrue(maybeProduct.isPresent());
        var product = maybeProduct.get();

        IntStream.range(1, 10).forEach(_ -> {
            product.dailyUpdate();
            assertEquals(0, product.getQuality());
        });

        product.dailyUpdate();
        assertEquals(1, product.getQuality());
    }

    @Test
    @DisplayName("the daily price of wine is not determined by quality")
    void wineDailyPriceIsStatic() {
        var maybeProduct = Product.of(
                "Wine",
                new EUR(1),
                10,
                ExpirationDate.DoesNotExpire.instance(),
                new WineRules()
        );

        assertTrue(maybeProduct.isPresent());
        var product = maybeProduct.get();

        assertCloseTo(new EUR(1), product.dailyPrice(), 0.0001);
    }

    @Test
    @DisplayName("bricks should not expire")
    void bricksShouldNotExpire() {
        assertTrue(
                Product.of(
                        "Bricks",
                        new EUR(1),
                        10,
                        new ExpirationDate.ExpiresAt(LocalDate.now()),
                        new BricksRules(LocalDate.now())
                ).isEmpty()
        );

        assertTrue(
                Product.of(
                        "Bricks",
                        new EUR(1),
                        10,
                        ExpirationDate.DoesNotExpire.instance(),
                        new BricksRules(LocalDate.now())
                ).isPresent()
        );
    }

    @Test
    @DisplayName("bricks quality should not exceed 9002")
    void bricksCreationFailsIfOverQualityThreshold() {
        assertTrue(
                Product.of(
                        "Bricks",
                        new EUR(1),
                        9002,
                        ExpirationDate.DoesNotExpire.instance(),
                        new BricksRules(LocalDate.now())
                ).isEmpty()
        );

        assertTrue(
                Product.of(
                        "Bricks",
                        new EUR(1),
                        9001,
                        ExpirationDate.DoesNotExpire.instance(),
                        new BricksRules(LocalDate.now())
                ).isPresent()
        );
    }

    @Test
    @DisplayName("bricks quality remains unchanged if product is still produced")
    void bricksQualityRemainsIfStillProduced() {
        var maybeProduct = Product.of(
                "Bricks",
                new EUR(1),
                10,
                ExpirationDate.DoesNotExpire.instance(),
                new BricksRules(LocalDate.now().plusDays(1))
        );

        assertTrue(maybeProduct.isPresent());
        var product = maybeProduct.get();
        product.dailyUpdate();

        assertEquals(10, product.getQuality());
    }

    @Test
    @DisplayName("bricks quality remains unchanged if production stopped less than a month ago")
    void bricksQualityRemainsIfNotYetOneMonthAfterEndOfProduction() {
        var maybeProduct = Product.of(
                "Bricks",
                new EUR(1),
                10,
                ExpirationDate.DoesNotExpire.instance(),
                new BricksRules(LocalDate.now().minusMonths(1))
        );

        assertTrue(maybeProduct.isPresent());
        var product = maybeProduct.get();
        product.dailyUpdate();

        assertEquals(10, product.getQuality());
    }

    @Test
    @DisplayName("bricks quality increases moderately if one month after production end")
    void bricksQualityIncreasesIfOneMonthOverEndOfProduction() {
        var maybeProduct = Product.of(
                "Bricks",
                new EUR(1),
                10,
                ExpirationDate.DoesNotExpire.instance(),
                new BricksRules(LocalDate.now().minusMonths(1).minusDays(1))
        );

        assertTrue(maybeProduct.isPresent());
        var product = maybeProduct.get();
        product.dailyUpdate();

        assertEquals(11, product.getQuality());
    }

    @Test
    @DisplayName("bricks quality increases moderately until one year after production end")
    void bricksQualityIncreasesIfAtOneYearOverEndOfProduction() {
        var maybeProduct = Product.of(
                "Bricks",
                new EUR(1),
                10,
                ExpirationDate.DoesNotExpire.instance(),
                new BricksRules(LocalDate.now().minusYears(1))
        );

        assertTrue(maybeProduct.isPresent());
        var product = maybeProduct.get();
        product.dailyUpdate();

        assertEquals(11, product.getQuality());
    }

    @Test
    @DisplayName("bricks quality increases largely if one year after production end")
    void bricksQualityIncreasesIfOneYearOverEndOfProduction() {
        var maybeProduct = Product.of(
                "Bricks",
                new EUR(1),
                10,
                ExpirationDate.DoesNotExpire.instance(),
                new BricksRules(LocalDate.now().minusYears(1).minusDays(1))
        );

        assertTrue(maybeProduct.isPresent());
        var product = maybeProduct.get();
        product.dailyUpdate();

        assertEquals(20, product.getQuality());
    }
}
