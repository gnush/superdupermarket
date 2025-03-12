package code.challenge.product.deprecated;

import code.challenge.SimulationContext;
import code.challenge.currency.Currency;

import java.time.LocalDate;
import java.util.Optional;

public final class Bricks extends NullableExpirationProduct {
    private static final int MAX_QUALITY = 9002;
    private final LocalDate endOfProduction;

    private Bricks(String label, Currency basePrice, int quality, LocalDate endOfProduction) {
        super(label, basePrice, quality);
        this.endOfProduction = endOfProduction;

        if (quality >= MAX_QUALITY)
            throw new IllegalArgumentException(String.format("The maximum allowed quality is %s, but was %s", MAX_QUALITY, quality));
    }

    @Override
    public boolean toRemove() {
        return false;
    }

    @Override
    public void dailyUpdate() {
        LocalDate today = LocalDate.now(SimulationContext.clock);
        if (today.minusYears(1).isAfter(endOfProduction))
            quality = Math.min(quality + 10, MAX_QUALITY);
        else if (today.minusMonths(1).isAfter(endOfProduction))
            quality = Math.min(quality + 1, MAX_QUALITY);
    }

    public static Optional<Bricks> of(String label, Currency basePrice, int quality, LocalDate endOfProduction) {
        try {
            return Optional.of(new Bricks(label, basePrice, quality, endOfProduction));
        } catch (IllegalArgumentException e) {
            System.err.printf("Brick set %s is not suitable, %s%n", label, e.getMessage());
            return Optional.empty();
        }
    }
}
