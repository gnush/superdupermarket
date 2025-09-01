package io.github.gnush.currency;

import io.github.gnush.SimulationContext;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record USD(BigDecimal amount) implements Currency {
    @Override
    @NotNull
    public Currency add(BigDecimal amount) {
        return new USD(this.amount.add(amount));
    }

    @Override
    @NotNull
    public Currency add(@NotNull Currency other) {
        if (getClass() == other.getClass()) {
            return new USD(amount.add(((USD) other).amount));
        } else {
            var exchangeRate = ExchangeRateService.exchangeRate(SimulationContext.now(), other.isoCode(), this.isoCode());

            if (exchangeRate.isPresent())
                return new USD(amount.add(other.amount().multiply(exchangeRate.get())));
            else
                throw new IllegalArgumentException("Cannot exchange " + other.isoCode() + " to " + isoCode());
        }
    }

    @Override
    public @NotNull String isoCode() {
        return "USD";
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

        if (amount.scale() == other.amount.scale())
            return this.amount.equals(other.amount);
        else if (amount.scale() > other.amount.scale())
            return this.amount.equals(other.amount.setScale(this.amount.scale(), RoundingMode.HALF_UP));
        else
            return this.amount.setScale(other.amount.scale(), RoundingMode.HALF_UP).equals(other.amount);
    }
}
