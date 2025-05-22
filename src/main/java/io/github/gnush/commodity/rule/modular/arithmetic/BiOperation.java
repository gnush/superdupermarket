package io.github.gnush.commodity.rule.modular.arithmetic;

import io.github.gnush.commodity.Commodity;
import io.github.gnush.commodity.rule.modular.Rule;
import io.github.gnush.commodity.rule.modular.functional.MergeRule;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.BiFunction;

public record BiOperation<N extends Number>(
        @NotNull Rule<Commodity, N> lhs,
        @NotNull Rule<Commodity, N> rhs,
        @NotNull BiFunction<N, N, Number> op
) implements Rule<Commodity, Number> {
    @Override
    public @NotNull Number apply(@NotNull Commodity x) {
        return new MergeRule<>(
                lhs,
                rhs,
                (lhs, rhs) -> {
                    if (lhs.getClass() != rhs.getClass())
                        throw new UnsupportedOperationException("unequal input types on BiOperation");
                    return op.apply(lhs, rhs);
                }
        ).apply(x);
    }

    public static <N extends Number> @NotNull BiOperation<N> plus(
            @NotNull Rule<Commodity, N> lhs,
            @NotNull Rule<Commodity, N> rhs
    ) throws UnsupportedOperationException {
        return new BiOperation<>(
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
                    default -> throw new UnsupportedOperationException();
                }
        );
    }

    public static <N extends Number> @NotNull BiOperation<N> multiply(
            @NotNull Rule<Commodity, N> lhs,
            @NotNull Rule<Commodity, N> rhs
    ) throws UnsupportedOperationException {
        return new BiOperation<>(
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
                    default -> throw new UnsupportedOperationException();
                }
        );
    }

    public static <N extends Number> @NotNull BiOperation<N> modulo(
            @NotNull Rule<Commodity, N> lhs,
            @NotNull Rule<Commodity, N> rhs
    ) throws UnsupportedOperationException {
        return new BiOperation<>(
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
                    default -> throw new UnsupportedOperationException();
                }
        );
    }
}