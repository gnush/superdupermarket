package code.challenge.commodity.rule.modular.update;

import code.challenge.commodity.Commodity;
import code.challenge.commodity.rule.modular.Rule;
import org.jetbrains.annotations.NotNull;

public record SetQuality(int quality) implements Rule<Commodity, Commodity> {
    @Override
    public @NotNull Commodity apply(@NotNull Commodity x) {
        x.setQuality(quality);
        return x;
    }
}
