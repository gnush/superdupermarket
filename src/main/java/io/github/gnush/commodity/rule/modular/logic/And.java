package io.github.gnush.commodity.rule.modular.logic;

import io.github.gnush.commodity.Commodity;
import io.github.gnush.commodity.rule.modular.Rule;
import io.github.gnush.commodity.rule.modular.functional.MergeRule;
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