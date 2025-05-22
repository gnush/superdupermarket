package io.github.gnush.commodity.rule;

import io.github.gnush.currency.Currency;
import io.github.gnush.commodity.ExpirationDate;
import io.github.gnush.commodity.Commodity;
import io.github.gnush.util.Tuple4;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface CommodityRules {
    boolean toRemove(@NotNull Commodity c);

    @NotNull Currency dailyPrice(@NotNull Commodity c);

    void dailyUpdate(@NotNull Commodity c);

    /**
     * @return A set of rules to check if object creation should be allowed
     */
    @NotNull List<Function<Tuple4<String, Currency, Integer, ExpirationDate>, Optional<String>>> creationRules();
}