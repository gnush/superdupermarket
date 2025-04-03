package code.challenge.product;

import code.challenge.SimulationContext;
import code.challenge.currency.Currency;

import java.time.LocalDate;
import java.util.Optional;

public final class Cheese extends NullableExpirationProduct {
    private static final int MIN_QUALITY = 30;
    private static final int EXPIRATION_DURATION_MIN = 50;
    private static final int EXPIRATION_DURATION_MAX = 100;

    public Cheese(String label, Currency basePrice, int quality, LocalDate expirationDate) {
        super(label, basePrice, quality, expirationDate);

        if(quality < MIN_QUALITY)
            throw new IllegalArgumentException("quality must be at least " + MIN_QUALITY);

        LocalDate today = LocalDate.now(SimulationContext.clock);
        if (expirationDate.isBefore(today.plusDays(EXPIRATION_DURATION_MIN)))
            throw new IllegalArgumentException(String.format("expiration date must be at least %s days from now", EXPIRATION_DURATION_MIN));

        if (expirationDate.isAfter(today.plusDays(EXPIRATION_DURATION_MAX)))
            throw new IllegalArgumentException(String.format("expiration date must be at most %s days from now",EXPIRATION_DURATION_MAX));
    }

    @Override
    public boolean toRemove() {
        return super.toRemove() || quality < MIN_QUALITY;
    }

    @Override
    public void dailyUpdate() {
        quality--;
    }

    public static Optional<Cheese> of(String label, Currency basePrice, int quality, LocalDate expirationDate) {
        try {
            return Optional.of(new Cheese(label, basePrice, quality, expirationDate));
        } catch (IllegalArgumentException e) {
            System.err.printf("Cheese %s is not suitable, %s%n", label, e.getMessage());
            return Optional.empty();
        }
    }
}