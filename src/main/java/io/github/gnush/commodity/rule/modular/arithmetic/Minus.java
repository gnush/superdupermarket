package io.github.gnush.commodity.rule.modular.arithmetic;

import io.github.gnush.commodity.Commodity;
import io.github.gnush.commodity.rule.modular.Rule;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;

public record Minus<N extends Number>(Rule<Commodity, N> rule) implements Rule<Commodity, Number> {
    @Override
    public @NotNull Number apply(@NotNull Commodity x) throws UnsupportedOperationException {
        var res = rule.apply(x);
        return switch (res) {
            case Byte       v -> -v;
            case Short      v -> -v;
            case Integer    v -> -v;
            case Long       v -> -v;
            case Float      v -> -v;
            case Double     v -> -v;
            case BigInteger v -> v.negate();
            case BigDecimal v -> v.negate();
            default -> throw new UnsupportedOperationException();
        };
    }
}