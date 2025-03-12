package code.challenge.product.deprecated;

import code.challenge.currency.Currency;

import java.time.LocalDate;

public final class Generic extends NullableExpirationProduct {
    public Generic(String label, Currency basePrice, int quality) {
        super(label, basePrice, quality);
    }

    public Generic(String label, Currency basePrice, int quality, LocalDate expirationDate) {
        super(label, basePrice, quality, expirationDate);
    }

    @Override
    public void dailyUpdate() { }
}