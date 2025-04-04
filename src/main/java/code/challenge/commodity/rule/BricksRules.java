package code.challenge.commodity.rule;

import code.challenge.SimulationContext;
import code.challenge.currency.Currency;
import code.challenge.commodity.ExpirationDate;
import code.challenge.commodity.Commodity;
import code.challenge.util.Tuple4;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class BricksRules extends GeneralRules {
    private static final int MAX_QUALITY = 9002;
    private final LocalDate endOfProduction;

    public BricksRules(@NotNull LocalDate endOfProduction) {
        this.endOfProduction = endOfProduction;
    }

    @Override
    public boolean toRemove(@NotNull Commodity c) {
        return false;
    }

    @Override
    public void dailyUpdate(@NotNull Commodity c) {
        LocalDate today = LocalDate.now(SimulationContext.clock);
        if (today.minusYears(1).isAfter(endOfProduction))
            c.setQuality(Math.min(c.getQuality() + 10, MAX_QUALITY));
        else if (today.minusMonths(1).isAfter(endOfProduction))
            c.setQuality(Math.min(c.getQuality() + 1, MAX_QUALITY));
    }

    @Override
    public @NotNull List<Function<Tuple4<String, Currency, Integer, ExpirationDate>, Optional<String>>> creationRules() {
        return List.of(
                p -> p._3() >= MAX_QUALITY
                        ? Optional.of(String.format("The maximum allowed quality is %s, but was %s", MAX_QUALITY, p._3()))
                        : Optional.empty(),
                p -> switch (p._4()) {
                    case ExpirationDate.DoesNotExpire _ -> Optional.empty();
                    case ExpirationDate.ExpiresAt _ -> Optional.of("cannot expire");
                }
        );
    }

    public static @NotNull Optional<BricksRules> parse(@NotNull List<String> args) {
        try {
            return Optional.of(new BricksRules(LocalDate.parse(args.getFirst())));
        } catch (Exception _) {
            return Optional.empty();
        }
    }
}
