package code.challenge.commodity.rule.modular;

import org.jetbrains.annotations.NotNull;

public interface Rule<I, O> {
    @NotNull O apply(@NotNull I x);
}