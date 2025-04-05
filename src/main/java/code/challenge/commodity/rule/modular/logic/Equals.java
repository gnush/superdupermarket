package code.challenge.commodity.rule.modular.logic;

import code.challenge.commodity.Commodity;
import code.challenge.commodity.ExpirationDate;
import code.challenge.commodity.rule.modular.Rule;
import org.jetbrains.annotations.NotNull;

public record Equals<T>(
        @NotNull T compareTo,
        @NotNull Rule<Commodity, T> get
) implements Rule<Commodity, Boolean> {
    @Override
    public @NotNull Boolean apply(@NotNull Commodity x) {
        return get.apply(x).equals(compareTo);
    }

    public static @NotNull Equals<String> label(@NotNull String compare) {
        return new Equals<>(compare, c -> c.label);
    }

    public static @NotNull Equals<Integer> quality(int compare) {
        return new Equals<>(compare, Commodity::getQuality);
    }

    public static @NotNull Equals<ExpirationDate> expirationDate(@NotNull ExpirationDate compare) {
        return new Equals<>(compare, c -> c.expirationDate);
    }
}