package code.challenge.commodity.rule.modular;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class SimpleRule<I, O> extends AbstractRule<I, O> {
    private final @NotNull Function<I, O> f;

    public SimpleRule(@NotNull Function<I, O> f) {
        this.f = f;
    }

    @Override
    public @NotNull Function<I, O> getRule() {
        return f;
    }

    public static <T> @NotNull SimpleRule<T, T> identity() {
        return new SimpleRule<>(x -> x);
    }
}