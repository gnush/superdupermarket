package code.challenge.commodity.rule.modular.logic;

import code.challenge.commodity.Commodity;
import code.challenge.commodity.rule.modular.Rule;
import org.jetbrains.annotations.NotNull;

public record Implies(
        Rule<Commodity, Boolean> lhs,
        Rule<Commodity, Boolean> rhs
) implements Rule<Commodity, Boolean> {
    @Override
    public @NotNull Boolean apply(@NotNull Commodity x) {
        return new Or(new Not(lhs), rhs).apply(x);
    }
}
