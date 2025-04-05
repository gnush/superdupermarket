package code.challenge.commodity.rule.modular.lit;

import code.challenge.commodity.rule.modular.Rule;
import org.jetbrains.annotations.NotNull;

public record Literal<I, O>(@NotNull O value) implements Rule<I, O> {
    @Override
    public @NotNull O apply(@NotNull I x) {
        return value;
    }

    public static <I, O> @NotNull Literal<I, O> of(O value) {
        return new Literal<>(value);
    }
}
