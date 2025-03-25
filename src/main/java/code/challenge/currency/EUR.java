package code.challenge.currency;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

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
    @NotNull
    public String toString() {
        return amount + " €";
    }
}