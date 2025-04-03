package code.challenge.product;

import code.challenge.SimulationContext;
import code.challenge.currency.Currency;

import java.math.BigDecimal;
import java.time.LocalDate;

public abstract class NullableExpirationProduct {
    public final String label;
    public final Currency basePrice;
    int quality;
    public final LocalDate expirationDate;

    NullableExpirationProduct(String label, Currency basePrice, int quality) {
        this.label = label;
        this.basePrice = basePrice;
        this.quality = quality;
        this.expirationDate = null;
    }

    NullableExpirationProduct(String label, Currency basePrice, int quality, LocalDate expirationDate) {
        this.label = label;
        this.basePrice = basePrice;
        this.quality = quality;
        this.expirationDate = expirationDate;
    }

    /**
     * Calculates the daily accurate price of the Product
     * @return The daily accurate price
     */
    public Currency dailyPrice() {
        return basePrice.add(BigDecimal.valueOf(0.1).multiply(BigDecimal.valueOf(quality)));
    }

    /**
     * Checks if the Product needs to be removed from the inventory
     * @return true if the Product should be removed, false otherwise.
     */
    public boolean toRemove() {
        if (expirationDate == null)
            return false;

        return LocalDate.now(SimulationContext.clock).isAfter(expirationDate);
    }

    /**
     * Age by one day
     */
    public abstract void dailyUpdate();

    public String pretty() {
        return String.format("%1$s (%2$s)", label, dailyPrice())
                + (expirationDate != null ? "\nExpiration Date: " + expirationDate : "");
    }

    public String overview() {
        return String.format("%s: price='%s' quality=%s remove=%s", label, dailyPrice(), quality, toRemove());
    }

    @Override
    public String toString() {
        return String.format("%s: basePrice='%s' quality=%s", label, basePrice, quality)
                + (expirationDate != null ? " expiresOn=%s" + expirationDate : "");
    }
}
