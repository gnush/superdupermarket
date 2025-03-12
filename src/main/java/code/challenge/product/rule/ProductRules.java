package code.challenge.product.rule;

import code.challenge.currency.Currency;
import code.challenge.product.ExpirationDate;
import code.challenge.product.Product;
import code.challenge.util.Tuple4;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface ProductRules {
    boolean toRemove(@NotNull Product p);

    @NotNull Currency dailyPrice(@NotNull Product p);

    void dailyUpdate(@NotNull Product p);

    /**
     * @return A set of rules to check if object creation should be allowed
     */
    @NotNull List<Function<Tuple4<String, Currency, Integer, ExpirationDate>, Optional<String>>> creationRules();
}