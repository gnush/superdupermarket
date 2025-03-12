package code.challenge.product.rule;

import code.challenge.SimulationContext;
import code.challenge.currency.Currency;
import code.challenge.product.ExpirationDate;
import code.challenge.product.Product;
import code.challenge.util.Tuple4;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class GeneralRules implements ProductRules {
    @Override
    public boolean toRemove(@NotNull Product p) {
        return switch (p.expirationDate) {
            case ExpirationDate.DoesNotExpire _ -> false;
            case ExpirationDate.ExpiresAt date -> LocalDate.now(SimulationContext.clock).isAfter(date.expirationDate());
        };
    }

    @Override
    public @NotNull Currency dailyPrice(@NotNull Product p) {
        return p.basePrice.add(0.1 * p.getQuality());
    }

    @Override
    public void dailyUpdate(@NotNull Product p) { }

    @Override
    public @NotNull List<Function<Tuple4<String, Currency, Integer, ExpirationDate>, Optional<String>>> creationRules() {
        return Collections.emptyList();
    }
}
