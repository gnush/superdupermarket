package code.challenge.product.rule;

import code.challenge.SimulationContext;
import code.challenge.currency.Currency;
import code.challenge.product.ExpirationDate;
import code.challenge.product.Product;
import code.challenge.util.Tuple4;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class CheeseRules extends GeneralRules {
    private static final int MIN_QUALITY = 30;
    private static final int EXPIRATION_DURATION_MIN = 50;
    private static final int EXPIRATION_DURATION_MAX = 100;

    @Override
    public boolean toRemove(@NotNull Product p) {
        return super.toRemove(p) || p.getQuality() < MIN_QUALITY;
    }

    @Override
    public void dailyUpdate(@NotNull Product p) {
        p.setQuality(p.getQuality()-1);
    }

    @Override
    public @NotNull List<Function<Tuple4<String, Currency, Integer, ExpirationDate>, Optional<String>>> creationRules() {
        LocalDate today = LocalDate.now(SimulationContext.clock);

        return List.of(
                p -> p._3() < MIN_QUALITY
                        ? Optional.of("quality must be at least " + MIN_QUALITY)
                        : Optional.empty(),
                p -> switch (p._4()) {
                    case ExpirationDate.DoesNotExpire _ -> Optional.of("cheese must expire at some point");
                    case ExpirationDate.ExpiresAt _ -> Optional.empty();
                },
                p -> switch (p._4()) {
                    case ExpirationDate.DoesNotExpire _ -> Optional.empty();
                    case ExpirationDate.ExpiresAt date -> date.expirationDate().isBefore(today.plusDays(EXPIRATION_DURATION_MIN))
                        ? Optional.of(String.format("expiration date must be at least %s days from now", EXPIRATION_DURATION_MIN))
                        : Optional.empty();
                },
                p -> switch (p._4()) {
                    case ExpirationDate.DoesNotExpire _ -> Optional.empty();
                    case ExpirationDate.ExpiresAt date -> date.expirationDate().isAfter(today.plusDays(EXPIRATION_DURATION_MAX))
                        ? Optional.of(String.format("expiration date must be at most %s days from now",EXPIRATION_DURATION_MAX))
                        : Optional.empty();
                }
        );
    }
}