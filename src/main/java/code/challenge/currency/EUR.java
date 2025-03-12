package code.challenge.currency;

import org.jetbrains.annotations.NotNull;

public record EUR(double amount) implements Currency {
    @Override
    @NotNull
    public Currency add(double amount) {
        return new EUR(this.amount + amount);
    }

    @Override
    @NotNull
    public Currency add(@NotNull Currency other) {
        if (getClass() == other.getClass())
            return new EUR(amount + ((EUR) other).amount);
        else
            throw new UnsupportedOperationException("Interchanging currencies is not supported");
    }

    @Override
    @NotNull
    public String toString() {
        return amount + " €";
    }
}