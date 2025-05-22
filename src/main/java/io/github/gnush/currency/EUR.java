package io.github.gnush.currency;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record EUR(BigDecimal amount) implements Currency {
    public EUR(double amount) {
        this(BigDecimal.valueOf(amount));
    }

    @Override
    @NotNull
    public Currency add(BigDecimal amount) {
        return new EUR(this.amount.add(amount));
    }

    @Override
    @NotNull
    public Currency add(@NotNull Currency other) {
        if (getClass() == other.getClass())
            return new EUR(amount.add(((EUR) other).amount));
        else
            throw new UnsupportedOperationException("Interchanging currencies is not supported");
    }

    @Override
    public @NotNull String currencyCode() {
        return "EUR";
    }

    @Override
    @NotNull
    public String toString() {
        return amount + " €";
    }

    @Override
    public boolean equals(Object o) {
        if (getClass() != o.getClass())
            return false;

        var other = (EUR) o;
        return this.amount.setScale(other.amount.scale(), RoundingMode.HALF_UP).equals(other.amount);
    }
}