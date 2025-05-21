package code.challenge.currency;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public sealed interface Currency permits EUR, USD {
    @NotNull
    Currency add(BigDecimal amount);

    @NotNull
    Currency add(@NotNull Currency other);

    @NotNull
    BigDecimal amount();

    @NotNull
    String currencyCode();
}
