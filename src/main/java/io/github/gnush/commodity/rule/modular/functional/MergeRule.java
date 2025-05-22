package io.github.gnush.commodity.rule.modular.functional;

import io.github.gnush.commodity.rule.modular.Rule;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

public class MergeRule<I, O1, O2, O> implements Rule<I, O> {
    private final @NotNull Rule<I, O1> lhs;
    private final @NotNull Rule<I, O2> rhs;
    private final @NotNull BiFunction<O1, O2, O> merge;

    public MergeRule(@NotNull Rule<I, O1> lhs, @NotNull Rule<I, O2> rhs, @NotNull BiFunction<O1, O2, O> merge) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.merge = merge;
    }

    @Override
    public @NotNull O apply(@NotNull I x) {
        return merge.apply(lhs.apply(x), rhs.apply(x));
    }
}
