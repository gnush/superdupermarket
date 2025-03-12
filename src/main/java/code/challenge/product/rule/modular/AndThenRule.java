package code.challenge.product.rule.modular;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class AndThenRule<I, T, O> extends AbstractRule<I, O> {
    private final @NotNull Rule<I, T> rule;
    private final @NotNull Function<T, O> after;

    public AndThenRule(@NotNull Rule<I, T> rule, @NotNull Function<T, O> after) {
        this.rule = rule;
        this.after = after;
    }

    @Override
    public @NotNull Function<I, O> getRule() {
        return rule.getRule().andThen(after);
    }
}
