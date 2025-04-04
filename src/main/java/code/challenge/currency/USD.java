package code.challenge.currency;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record USD(BigDecimal amount) implements Currency {
    public USD(double amount) {
        this(BigDecimal.valueOf(amount));
    }

    @Override
    @NotNull
    public Currency add(BigDecimal amount) {
        return new USD(this.amount.add(amount));
    }

    @Override
    @NotNull
    public Currency add(@NotNull Currency other) {
        if (getClass() == other.getClass())
            return new USD(amount.add(((USD) other).amount));
        else
            throw new UnsupportedOperationException("Interchanging currencies is not supported");
    }

    @Override
    @NotNull
    public String toString() {
        return "$"+amount;
    }

    @Override
    public boolean equals(Object o) {
        if (getClass() != o.getClass())
            return false;

        var other = (USD) o;
        return this.amount.setScale(other.amount.scale(), RoundingMode.HALF_UP).equals(other.amount);
    }
}
