package code.challenge.product.rule.modular;

import org.jetbrains.annotations.NotNull;

public abstract class AbstractRule<I, O> implements Rule<I, O> {
    @Override
    public @NotNull O apply(@NotNull I x) {
        return getRule().apply(x);
    }
}
