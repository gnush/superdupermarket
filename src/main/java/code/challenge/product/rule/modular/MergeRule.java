package code.challenge.product.rule.modular;

import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;
import java.util.function.Function;

public class MergeRule<I, O1, O2, O> extends AbstractRule<I, O> {
    private final @NotNull Rule<I, O1> lhs;
    private final @NotNull Rule<I, O2> rhs;
    private final @NotNull BiFunction<O1, O2, O> merge;

    public MergeRule(@NotNull Rule<I, O1> lhs, @NotNull Rule<I, O2> rhs, @NotNull BiFunction<O1, O2, O> merge) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.merge = merge;
    }

    @Override
    public @NotNull Function<I, O> getRule() {
        return x -> merge.apply(lhs.apply(x), rhs.apply(x));
    }
}
