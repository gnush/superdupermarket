package code.challenge.product.parse;

import code.challenge.currency.Currency;
import code.challenge.currency.EUR;
import code.challenge.currency.USD;
import code.challenge.product.ExpirationDate;
import code.challenge.product.Product;
import code.challenge.product.ProductLookup;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public final class Parse {
    @NotNull
    public static Optional<Product> product(@NotNull String type, @NotNull List<String> ruleArgs, @NotNull List<String> productArgs) {
        try {
            ExpirationDate expirationDate = productArgs.size() > 4
                    ? new ExpirationDate.ExpiresAt(LocalDate.parse(productArgs.get(4)))
                    : ExpirationDate.DoesNotExpire.instance();
            return Product.of(
                    productArgs.getFirst(),
                    parseCurrency(productArgs.get(1), productArgs.get(2)),
                    Integer.parseInt(productArgs.get(3)),
                    expirationDate,
                    ProductLookup.getProductRules(type, ruleArgs).get()
            );
        } catch (Exception _) {
            return Optional.empty();
        }
    }

    @NotNull
    private static Currency parseCurrency(@NotNull String TYPE, @NotNull String amount) throws NumberFormatException {
        double _amount = Double.parseDouble(amount);

        return switch (TYPE.toUpperCase()) {
            case "EUR" -> new EUR(_amount);
            case "USD" -> new USD(_amount);
            default -> throw new IllegalArgumentException(TYPE + " is not supported");
        };
    }
}
