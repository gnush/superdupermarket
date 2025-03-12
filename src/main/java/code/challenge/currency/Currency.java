package code.challenge.currency;

import org.jetbrains.annotations.NotNull;

public sealed interface Currency permits EUR, USD {
    @NotNull
    Currency add(double amount);

    @NotNull
    Currency add(@NotNull Currency other);
}
