package code.challenge.commodity.rule.modular.logic;

import code.challenge.commodity.Commodity;
import code.challenge.commodity.ExpirationDate;
import code.challenge.commodity.rule.modular.Rule;
import org.jetbrains.annotations.NotNull;

public record ExpirationDateEquals(
        ExpirationDate compare
) implements Rule<Commodity, Boolean> {
    @Override
    public @NotNull Boolean apply(@NotNull Commodity x) {
        return x.expirationDate.equals(compare);
    }
}