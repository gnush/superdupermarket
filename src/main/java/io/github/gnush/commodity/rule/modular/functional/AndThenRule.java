package io.github.gnush.commodity.rule.modular.functional;

import io.github.gnush.commodity.rule.modular.Rule;
import org.jetbrains.annotations.NotNull;

public class AndThenRule<I, T, O> implements Rule<I, O> {
    private final @NotNull Rule<I, T> rule;
    private final @NotNull Rule<T, O> after;

    public AndThenRule(@NotNull Rule<I, T> rule, @NotNull Rule<T, O> after) {
        this.rule = rule;
        this.after = after;
    }

    @Override
    public @NotNull O apply(@NotNull I x) {
        return after.apply(rule.apply(x));
    }
}
