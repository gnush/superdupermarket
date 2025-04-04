package code.challenge.commodity.rule.modular;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class ComposeRule<I, T, O> extends AbstractRule<I, O> {
    private final @NotNull Rule<T, O> rule;
    private final @NotNull Function<I, T> before;

    public ComposeRule(@NotNull Rule<T, O> rule, @NotNull Function<I, T> before) {
        this.rule = rule;
        this.before = before;
    }

    @Override
    public @NotNull Function<I, O> getRule() {
        return rule.getRule().compose(before);
    }
}
