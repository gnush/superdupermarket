package code.challenge.product;

import code.challenge.currency.Currency;

import java.util.Optional;

public final class Wine extends NullableExpirationProduct {
    private static final int MIN_QUALITY = 0;
    private static final int MAX_AGING_QUALITY = 50;
    private int agingInterval = 10;

    public Wine(String label, Currency basePrice, int quality) {
        super(label, basePrice, quality);

        if(quality < MIN_QUALITY)
            throw new IllegalArgumentException("Quality must be at least " + MIN_QUALITY);
    }

    @Override
    public Currency dailyPrice() {
        return basePrice;
    }

    @Override
    public boolean toRemove() {
        return false;
    }

    @Override
    public void dailyUpdate() {
        if (quality < MAX_AGING_QUALITY) {
            agingInterval--;

            if (agingInterval > 0) {
                quality = Math.min(quality+1, MAX_AGING_QUALITY);
                agingInterval = 10;
            }
        }
    }

    public static Optional<Wine> of(String label, Currency basePrice, int quality) {
        try {
            return Optional.of(new Wine(label, basePrice, quality));
        } catch (IllegalArgumentException e) {
            System.err.printf("Wine %s is not suitable, %s%n", label, e.getMessage());
            return Optional.empty();
        }
    }
}
