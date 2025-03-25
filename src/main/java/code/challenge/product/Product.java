package code.challenge.product;

import code.challenge.SimulationContext;
import code.challenge.currency.Currency;
import code.challenge.observer.Observable;
import code.challenge.observer.ProductChange;
import code.challenge.product.rule.ProductRules;
import code.challenge.util.Tuple4;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class Product extends Observable<Product, ProductChange> {
    @NotNull public final String label;
    @NotNull public final Currency basePrice;
    @NotNull public final ExpirationDate expirationDate;

    private int quality;
    @NotNull private final ProductRules rules;

    @NotNull private LocalDate lastDailyUpdate = LocalDate.now(SimulationContext.clock);

    private Product(@NotNull String label, @NotNull Currency basePrice, int quality, @NotNull ExpirationDate expirationDate, @NotNull ProductRules rules) {
        this.label = label;
        this.basePrice = basePrice;
        this.quality = quality;
        this.expirationDate = expirationDate;
        this.rules = rules;
    }

    /**
     * Calculates the daily accurate price of the Product
     * @return The daily accurate price
     */
    public @NotNull Currency dailyPrice() {
        return rules.dailyPrice(this);
    }

    /**
     * Checks if the Product needs to be removed from the inventory
     * @return true if the Product should be removed, false otherwise.
     */
    public boolean toRemove() {
        return switch (expirationDate) {
            case ExpirationDate.DoesNotExpire _ -> false;
            case ExpirationDate.ExpiresAt _ -> rules.toRemove(this);
        };
    }

    /**
     * Age by one day
     */
    public void dailyUpdate() {
        var today = LocalDate.now(SimulationContext.clock);
        if (lastDailyUpdate.isBefore(today)) {
            rules.dailyUpdate(this);
            lastDailyUpdate = today;
            notifyObservers(this, ProductChange.DailyUpdate.getInstance());
        }
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        int change = this.quality-quality;

        this.quality = quality;

        if (change < 0)
            this.notifyObservers(this, new ProductChange.QualityIncrease(Math.abs(change)));
        else if (change > 0)
            this.notifyObservers(this, new ProductChange.QualityDecrease(change));
    }

    public @NotNull String pretty() {
        return String.format("%s (%s)\n%s", label, dailyPrice(), expirationDate);
    }

    public @NotNull String overview() {
        return String.format("%s: price='%s' quality=%s remove=%s", label, dailyPrice(), quality, toRemove());
    }

    @Override
    public @NotNull String toString() {
        return String.format("%s: basePrice='%s' quality=%s, expiresOn=%s", label, basePrice, quality, expirationDate);
    }

    @NotNull
    public static Optional<Product> of(@NotNull String label, @NotNull Currency basePrice, int quality, @NotNull ProductRules rules) {
        return of(label, basePrice, quality, ExpirationDate.DoesNotExpire.instance(), rules);
    }

    @NotNull
    public static Optional<Product> of(@NotNull String label, @NotNull Currency basePrice, int quality, @NotNull ExpirationDate expirationDate, @NotNull ProductRules rules) {
        List<String> errors = rules.creationRules().stream()
                .map(f -> f.apply(new Tuple4<>(label, basePrice, quality, expirationDate)))
                .filter(Optional::isPresent).map(Optional::get).toList();

        if (!errors.isEmpty()) {
            System.err.printf("Error during creation of product %s:%n", label);
            errors.forEach(s -> System.err.println("\t"+s));
            return Optional.empty();
        }

        return Optional.of(new Product(label, basePrice, quality, expirationDate, rules));
    }
}
