package code.challenge.product;

import code.challenge.product.rule.GeneralRules;
import code.challenge.product.rule.ProductRules;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public final class ProductLookup {
    private static final HashMap<String, Function<List<String>, Optional<? extends ProductRules>>> productRulesLookup = new HashMap<>();

    public static void register(@NotNull String productType, @NotNull Function<List<String>, Optional<? extends ProductRules>> productRules) {
        productRulesLookup.put(productType.toLowerCase(), productRules);
    }

    public static void remove(String productType) {
        productRulesLookup.remove(productType.toLowerCase());
    }

    @NotNull
    public static Optional<? extends ProductRules> getProductRules(@NotNull String of, @NotNull List<String> args) {
        return productRulesLookup.getOrDefault(of.toLowerCase(), _ -> Optional.of(new GeneralRules())).apply(args);
    }
}
