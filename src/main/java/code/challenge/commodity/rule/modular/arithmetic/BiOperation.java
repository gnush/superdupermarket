package code.challenge.commodity.rule.modular.arithmetic;

import code.challenge.commodity.Commodity;
import code.challenge.commodity.rule.modular.Rule;
import code.challenge.commodity.rule.modular.functional.MergeRule;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.function.BiFunction;

public record BiOperation(
        Rule<Commodity, Number> lhs,
        Rule<Commodity, Number> rhs,
        BiFunction<Number, Number, Number> op
) implements Rule<Commodity, Number> {
    @Override
    public @NotNull Number apply(@NotNull Commodity x) {
        return new MergeRule<>(
                lhs,
                rhs,
                (lhs, rhs) -> {
                    if (lhs.getClass() != rhs.getClass())
                        return lhs;
                    return op.apply(lhs, rhs);
                }
        ).apply(x);
    }

    public static BiOperation Plus(
            Rule<Commodity, Number> lhs,
            Rule<Commodity, Number> rhs
    ) {
        return new BiOperation(
                lhs,
                rhs,
                (a, b) -> switch (a) {
                    case Byte       v -> v+(Byte) b;
                    case Short      v -> v+(Short) b;
                    case Integer    v -> v+(Integer) b;
                    case Long       v -> v+(Long) b;
                    case Float      v -> v+(Float) b;
                    case Double     v -> v+(Double) b;
                    case BigInteger v -> v.add((BigInteger) b);
                    case BigDecimal v -> v.add((BigDecimal) b);
                    default -> a;
                }
        );
    }

    public static BiOperation Multiply(
            Rule<Commodity, Number> lhs,
            Rule<Commodity, Number> rhs
    ) {
        return new BiOperation(
                lhs,
                rhs,
                (a, b) -> switch (a) {
                    case Byte       v -> v*(Byte) b;
                    case Short      v -> v*(Short) b;
                    case Integer    v -> v*(Integer) b;
                    case Long       v -> v*(Long) b;
                    case Float      v -> v*(Float) b;
                    case Double     v -> v*(Double) b;
                    case BigInteger v -> v.multiply((BigInteger) b);
                    case BigDecimal v -> v.multiply((BigDecimal) b);
                    default -> a;
                }
        );
    }

    public static BiOperation Divide(
            Rule<Commodity, Number> lhs,
            Rule<Commodity, Number> rhs
    ) {
        return new BiOperation(
                lhs,
                rhs,
                (a, b) -> switch (a) {
                    case Byte       v -> v/(Byte) b;
                    case Short      v -> v/(Short) b;
                    case Integer    v -> v/(Integer) b;
                    case Long       v -> v/(Long) b;
                    case Float      v -> v/(Float) b;
                    case Double     v -> v/(Double) b;
                    case BigInteger v -> v.divide((BigInteger) b);
                    case BigDecimal v -> v.divide((BigDecimal) b, RoundingMode.HALF_UP);
                    default -> a;
                }
        );
    }

    public static BiOperation Modulo(
            Rule<Commodity, Number> lhs,
            Rule<Commodity, Number> rhs
    ) {
        return new BiOperation(
                lhs,
                rhs,
                (a, b) -> switch (a) {
                    case Byte       v -> v%(Byte) b;
                    case Short      v -> v%(Short) b;
                    case Integer    v -> v%(Integer) b;
                    case Long       v -> v%(Long) b;
                    case Float      v -> v%(Float) b;
                    case Double     v -> v%(Double) b;
                    case BigInteger v -> v.mod((BigInteger) b);
                    case BigDecimal v -> v.remainder((BigDecimal) b).abs();
                    default -> a;
                }
        );
    }
}