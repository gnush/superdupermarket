package code.challenge.commodity.rule.modular.arithmetic;

import code.challenge.commodity.Commodity;
import code.challenge.commodity.rule.modular.Rule;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;

public record Minus(Rule<Commodity, Number> rule) implements Rule<Commodity, Number> {
    @Override
    public @NotNull Number apply(@NotNull Commodity x) {
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
            default -> res;
        };
    }
}