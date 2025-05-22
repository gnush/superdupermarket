package io.github.gnush.commodity;

import io.github.gnush.SimulationContext;
import io.github.gnush.currency.Currency;
import io.github.gnush.observer.Observable;
import io.github.gnush.observer.CommodityChange;
import io.github.gnush.commodity.rule.CommodityRules;
import io.github.gnush.util.Tuple4;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class Commodity extends Observable<Commodity, CommodityChange> {
    @NotNull public final String label;
    @NotNull public final Currency basePrice;
    @NotNull public final ExpirationDate expirationDate;

    private int quality;
    @NotNull private final CommodityRules rules;

    @NotNull private LocalDate lastDailyUpdate = LocalDate.now(SimulationContext.clock);

    private Commodity(@NotNull String label, @NotNull Currency basePrice, int quality, @NotNull ExpirationDate expirationDate, @NotNull CommodityRules rules) {
        this.label = label;
        this.basePrice = basePrice;
        this.quality = quality;
        this.expirationDate = expirationDate;
        this.rules = rules;
    }

    /**
     * Calculates the daily accurate price of the Commodity
     * @return The daily accurate price
     */
    public @NotNull Currency dailyPrice() {
        return rules.dailyPrice(this);
    }

    /**
     * Checks if the Commodity needs to be removed from the inventory
     * @return true if the Commodity should be removed, false otherwise.
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
            notifyObservers(this, CommodityChange.DailyUpdate.getInstance());
        }
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        int change = this.quality-quality;

        this.quality = quality;

        if (change < 0)
            this.notifyObservers(this, new CommodityChange.QualityIncrease(Math.abs(change)));
        else if (change > 0)
            this.notifyObservers(this, new CommodityChange.QualityDecrease(change));
    }

    public @NotNull String pretty() {
        return String.format("%s (%s)\n%s", label, dailyPrice(), expirationDate);
    }

    public @NotNull String overview() {
        return String.format("%s: price='%s' quality=%s remove=%s", label, dailyPrice(), quality, toRemove());
    }

    @Override
    public @NotNull String toString() {
        return String.format("%s: basePrice='%s' quality=%s expiresOn=%s", label, basePrice, quality, expirationDate);
    }

    @NotNull
    public static Optional<Commodity> of(@NotNull String label, @NotNull Currency basePrice, int quality, @NotNull CommodityRules rules) {
        return of(label, basePrice, quality, ExpirationDate.DoesNotExpire.instance(), rules);
    }

    @NotNull
    public static Optional<Commodity> of(@NotNull String label, @NotNull Currency basePrice, int quality, @NotNull ExpirationDate expirationDate, @NotNull CommodityRules rules) {
        List<String> errors = rules.creationRules().stream()
                .map(f -> f.apply(new Tuple4<>(label, basePrice, quality, expirationDate)))
                .filter(Optional::isPresent).map(Optional::get).toList();

        if (!errors.isEmpty()) {
            System.err.printf("Error during creation of commodity %s:%n", label);
            errors.forEach(s -> System.err.println("\t"+s));
            return Optional.empty();
        }

        return Optional.of(new Commodity(label, basePrice, quality, expirationDate, rules));
    }
}
