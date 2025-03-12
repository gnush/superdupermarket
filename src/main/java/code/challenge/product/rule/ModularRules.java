package code.challenge.product.rule;

import code.challenge.currency.Currency;
import code.challenge.product.ExpirationDate;
import code.challenge.product.Product;
import code.challenge.product.rule.modular.Rule;
import code.challenge.util.Tuple4;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public record ModularRules(
        @NotNull Rule<Product, Boolean> toRemove,
        @NotNull Rule<Product, Currency> dailyPrice,
        @NotNull Consumer<Product> dailyUpdate,
        @NotNull List<Function<Tuple4<String, Currency, Integer, ExpirationDate>, Optional<String>>> creationRules
) implements ProductRules {
    public ModularRules(
            @NotNull Rule<Product, Boolean> toRemove,
            @NotNull Rule<Product, Currency> dailyPrice,
            @NotNull Consumer<Product> dailyUpdate
    ) {
        this(toRemove, dailyPrice, dailyUpdate, Collections.emptyList());
    }

    @Override
    public boolean toRemove(@NotNull Product p) {
        return toRemove.apply(p);
    }

    @Override
    public @NotNull Currency dailyPrice(@NotNull Product p) {
        return dailyPrice.apply(p);
    }

    @Override
    public void dailyUpdate(@NotNull Product p) {
        dailyUpdate.accept(p);
    }

    @Override
    public @NotNull List<Function<Tuple4<String, Currency, Integer, ExpirationDate>, Optional<String>>> creationRules() {
        return creationRules;
    }
}
