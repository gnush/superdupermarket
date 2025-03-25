package code.challenge.product.deprecated;

import code.challenge.currency.Currency;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

public abstract sealed class SealedProduct permits SealedProduct.Lasting, SealedProduct.Expirable {
    public final String label;
    public final Currency basePrice;
    public int quality;

    private SealedProduct(String label, Currency basePrice, int quality) {
        this.label = label;
        this.basePrice = basePrice;
        this.quality = quality;
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
    public abstract boolean toRemove();

    /**
     * Age by one day
     */
    public abstract void dailyUpdate();

    public String pretty() {
        return String.format("%1$s (%2$s)", label, dailyPrice());
    }

    static abstract sealed class Lasting extends SealedProduct permits SealedProduct.Wine {
        Lasting(String label, Currency basePrice, int quality) {
            super(label, basePrice, quality);
        }
    }

    static abstract sealed class Expirable extends SealedProduct permits SealedProduct.Generic, SealedProduct.Cheese {
        final LocalDate expirationDate;

        Expirable(String label, Currency basePrice, int quality, LocalDate expirationDate) {
            super(label, basePrice, quality);
            this.expirationDate = expirationDate;
        }

        @Override
        public boolean toRemove() {
            return LocalDate.now().isAfter(expirationDate);
        }

        @Override
        public String pretty() {
            return super.pretty()
                    + "\nExpiration Date: " + expirationDate;
        }
    }

    // Exact same definitions as with the other approach
    static final class Generic extends Expirable {
        public Generic(String label, Currency basePrice, int quality, LocalDate expirationDate) {
            super(label, basePrice, quality, expirationDate);
        }

        @Override
        public void dailyUpdate() { }
    }

    static final class Cheese extends Expirable {
        private static final int MIN_QUALITY = 30;
        private static final int EXPIRATION_DURATION_MIN = 50;
        private static final int EXPIRATION_DURATION_MAX = 100;

        public Cheese(String label, Currency basePrice, int quality, LocalDate expirationDate) {
            super(label, basePrice, quality, expirationDate);

            if(quality < MIN_QUALITY)
                throw new IllegalArgumentException("quality must be at least " + MIN_QUALITY);

            LocalDate today = LocalDate.now();
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

        public static Optional<code.challenge.product.deprecated.Cheese> of(String label, Currency basePrice, int quality, LocalDate expirationDate) {
            try {
                return Optional.of(new code.challenge.product.deprecated.Cheese(label, basePrice, quality, expirationDate));
            } catch (IllegalArgumentException e) {
                System.err.printf("Cheese %s is not suitable, %s%n", label, e.getMessage());
                return Optional.empty();
            }
        }
    }

    static final class Wine extends Lasting {
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

                if (agingInterval == 0) {
                    quality = Math.min(quality+1, MAX_AGING_QUALITY);
                    agingInterval = 10;
                }
            }
        }

        public static Optional<code.challenge.product.deprecated.Wine> of(String label, Currency basePrice, int quality) {
            try {
                return Optional.of(new code.challenge.product.deprecated.Wine(label, basePrice, quality));
            } catch (IllegalArgumentException e) {
                System.err.printf("Wine %s is not suitable, %s%n", label, e.getMessage());
                return Optional.empty();
            }
        }
    }
}