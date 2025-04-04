package code.challenge.commodity.rule;

import code.challenge.currency.Currency;
import code.challenge.commodity.ExpirationDate;
import code.challenge.commodity.Commodity;
import code.challenge.commodity.rule.modular.Rule;
import code.challenge.util.Tuple4;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public record ModularRules(
        @NotNull Rule<Commodity, Boolean> toRemove,
        @NotNull Rule<Commodity, Currency> dailyPrice,
        @NotNull Consumer<Commodity> dailyUpdate,
        @NotNull List<Function<Tuple4<String, Currency, Integer, ExpirationDate>, Optional<String>>> creationRules
) implements CommodityRules {
    public ModularRules(
            @NotNull Rule<Commodity, Boolean> toRemove,
            @NotNull Rule<Commodity, Currency> dailyPrice,
            @NotNull Consumer<Commodity> dailyUpdate
    ) {
        this(toRemove, dailyPrice, dailyUpdate, Collections.emptyList());
    }

    @Override
    public boolean toRemove(@NotNull Commodity c) {
        return toRemove.apply(c);
    }

    @Override
    public @NotNull Currency dailyPrice(@NotNull Commodity c) {
        return dailyPrice.apply(c);
    }

    @Override
    public void dailyUpdate(@NotNull Commodity c) {
        dailyUpdate.accept(c);
    }

    @Override
    public @NotNull List<Function<Tuple4<String, Currency, Integer, ExpirationDate>, Optional<String>>> creationRules() {
        return creationRules;
    }
}
