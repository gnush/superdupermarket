package code.challenge.commodity.rule.modular;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface Rule<I, O> {
    @NotNull Function<I, O> getRule();
    @NotNull O apply(@NotNull I x);
}