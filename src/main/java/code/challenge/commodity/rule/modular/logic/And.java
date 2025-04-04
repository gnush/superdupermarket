package code.challenge.commodity.rule.modular.logic;

import code.challenge.commodity.Commodity;
import code.challenge.commodity.rule.modular.Rule;
import code.challenge.commodity.rule.modular.functional.MergeRule;
import org.jetbrains.annotations.NotNull;

public record And(
        Rule<Commodity, Boolean> lhs,
        Rule<Commodity, Boolean> rhs
) implements Rule<Commodity, Boolean> {
    @Override
    public @NotNull Boolean apply(@NotNull Commodity x) {
        return new MergeRule<>(lhs, rhs, Boolean::logicalAnd).apply(x);
    }
}