package code.challenge.commodity.rule.modular.logic;

import code.challenge.commodity.Commodity;
import code.challenge.commodity.rule.modular.Rule;
import org.jetbrains.annotations.NotNull;

public record Not(Rule<Commodity, Boolean> rule) implements Rule<Commodity, Boolean> {
    @Override
    public @NotNull Boolean apply(@NotNull Commodity x) {
        return !rule.apply(x);
    }
}
