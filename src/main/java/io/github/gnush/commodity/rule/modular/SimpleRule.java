package io.github.gnush.commodity.rule.modular;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class SimpleRule<I, O> implements Rule<I, O> {
    private final @NotNull Function<I, O> f;

    public SimpleRule(@NotNull Function<I, O> f) {
        this.f = f;
    }

    @Override
    public @NotNull O apply(@NotNull I x) {
        return f.apply(x);
    }

    public static <T> @NotNull SimpleRule<T, T> identity() {
        return new SimpleRule<>(x -> x);
    }
}