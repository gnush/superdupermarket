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

public class BricksRules extends GeneralRules {
    private static final int MAX_QUALITY = 9002;
    private final LocalDate endOfProduction;

    public BricksRules(@NotNull LocalDate endOfProduction) {
        this.endOfProduction = endOfProduction;
    }

    @Override
    public boolean toRemove(@NotNull Product p) {
        return false;
    }

    @Override
    public void dailyUpdate(@NotNull Product p) {
        LocalDate today = LocalDate.now(SimulationContext.clock);
        if (today.minusYears(1).isAfter(endOfProduction))
            p.setQuality(Math.min(p.getQuality() + 10, MAX_QUALITY));
        else if (today.minusMonths(1).isAfter(endOfProduction))
            p.setQuality(Math.min(p.getQuality() + 1, MAX_QUALITY));
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
