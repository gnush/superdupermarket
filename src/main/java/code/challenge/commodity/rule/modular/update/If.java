package code.challenge.commodity.rule.modular.update;

import code.challenge.commodity.Commodity;
import code.challenge.commodity.rule.modular.Rule;
import org.jetbrains.annotations.NotNull;

public record If(
        Rule<Commodity, Boolean> test,
        Rule<Commodity, Commodity> then,
        Rule<Commodity, Commodity> els
) implements Rule<Commodity, Commodity> {
    @Override
    public @NotNull Commodity apply(@NotNull Commodity x) {
        return test.apply(x) ? then.apply(x) : els.apply(x);
    }
}
