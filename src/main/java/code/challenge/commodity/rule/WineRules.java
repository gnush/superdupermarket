package code.challenge.commodity.rule;

import code.challenge.currency.Currency;
import code.challenge.commodity.ExpirationDate;
import code.challenge.commodity.Commodity;
import code.challenge.util.Tuple4;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class WineRules implements CommodityRules {
    private static final int MIN_QUALITY = 0;
    private static final int MAX_AGING_QUALITY = 50;
    private int agingInterval = 10;

    @Override
    public boolean toRemove(@NotNull Commodity c) {
        return false;
    }

    @Override
    public @NotNull Currency dailyPrice(@NotNull Commodity c) {
        return c.basePrice;
    }

    @Override
    public void dailyUpdate(@NotNull Commodity c) {
        if (c.getQuality() < MAX_AGING_QUALITY)
            agingInterval--;

        if (agingInterval == 0) {
            c.setQuality(Math.min(c.getQuality()+1, MAX_AGING_QUALITY));
            agingInterval = 10;
        }
    }

    @Override
    public @NotNull List<Function<Tuple4<String, Currency, Integer, ExpirationDate>, Optional<String>>> creationRules() {
        return List.of(
                x -> switch (x._4()) {
                    case ExpirationDate.DoesNotExpire _ -> Optional.empty();
                    case ExpirationDate.ExpiresAt _ -> Optional.of("cannot expire");
                },
                x -> x._3() < MIN_QUALITY ? Optional.of("quality must be at least " + MIN_QUALITY) : Optional.empty()
        );
    }
}
