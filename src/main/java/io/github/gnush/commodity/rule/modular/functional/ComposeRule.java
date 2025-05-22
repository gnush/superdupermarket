package io.github.gnush.commodity.rule.modular.functional;

import io.github.gnush.commodity.rule.modular.Rule;
import org.jetbrains.annotations.NotNull;

public class ComposeRule<I, T, O> implements Rule<I, O> {
    private final @NotNull Rule<T, O> rule;
    private final @NotNull Rule<I, T> before;

    public ComposeRule(@NotNull Rule<T, O> rule, @NotNull Rule<I, T> before) {
        this.rule = rule;
        this.before = before;
    }

    @Override
    public @NotNull O apply(@NotNull I x) {
        return rule.apply(before.apply(x));
    }
}
