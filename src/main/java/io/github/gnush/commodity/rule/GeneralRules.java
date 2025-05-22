package io.github.gnush.commodity.rule;

import io.github.gnush.SimulationContext;
import io.github.gnush.currency.Currency;
import io.github.gnush.commodity.ExpirationDate;
import io.github.gnush.commodity.Commodity;
import io.github.gnush.util.Tuple4;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class GeneralRules implements CommodityRules {
    @Override
    public boolean toRemove(@NotNull Commodity c) {
        return switch (c.expirationDate) {
            case ExpirationDate.DoesNotExpire _ -> false;
            case ExpirationDate.ExpiresAt date -> LocalDate.now(SimulationContext.clock).isAfter(date.expirationDate());
        };
    }

    @Override
    public @NotNull Currency dailyPrice(@NotNull Commodity c) {
        return c.basePrice.add(BigDecimal.valueOf(0.1).multiply(BigDecimal.valueOf(c.getQuality())));
    }

    @Override
    public void dailyUpdate(@NotNull Commodity c) { }

    @Override
    public @NotNull List<Function<Tuple4<String, Currency, Integer, ExpirationDate>, Optional<String>>> creationRules() {
        return Collections.emptyList();
    }
}
