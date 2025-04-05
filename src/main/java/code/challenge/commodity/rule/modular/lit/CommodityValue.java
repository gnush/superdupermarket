package code.challenge.commodity.rule.modular.lit;

import code.challenge.commodity.Commodity;
import code.challenge.commodity.ExpirationDate;
import code.challenge.commodity.rule.modular.Rule;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public record CommodityValue<T>(
        @NotNull Function<Commodity, T> accessor
) implements Rule<Commodity, T> {
    @Override
    public @NotNull T apply(@NotNull Commodity x) {
        return accessor.apply(x);
    }

    public static @NotNull CommodityValue<String> label() {
        return new CommodityValue<>(c -> c.label);
    }

    public static @NotNull CommodityValue<Integer> quality() {
        return new CommodityValue<>(Commodity::getQuality);
    }

    public static @NotNull CommodityValue<ExpirationDate> expirationDate() {
        return new CommodityValue<>(c -> c.expirationDate);
    }
}
