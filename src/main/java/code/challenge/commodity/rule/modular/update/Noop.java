package code.challenge.commodity.rule.modular.update;

import code.challenge.commodity.Commodity;
import code.challenge.commodity.rule.modular.Rule;
import org.jetbrains.annotations.NotNull;

public record Noop() implements Rule<Commodity, Commodity> {
    @Override
    public @NotNull Commodity apply(@NotNull Commodity x) {
        return x;
    }
}
