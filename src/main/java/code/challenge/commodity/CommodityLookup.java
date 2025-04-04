package code.challenge.commodity;

import code.challenge.commodity.rule.GeneralRules;
import code.challenge.commodity.rule.CommodityRules;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public final class CommodityLookup {
    private static final HashMap<String, Function<List<String>, Optional<? extends CommodityRules>>> commodityRulesLookup = new HashMap<>();

    public static void register(@NotNull String commodityType, @NotNull Function<List<String>, Optional<? extends CommodityRules>> commodityRules) {
        commodityRulesLookup.put(commodityType.toLowerCase(), commodityRules);
    }

    public static void remove(String commodityType) {
        commodityRulesLookup.remove(commodityType.toLowerCase());
    }

    @NotNull
    public static Optional<? extends CommodityRules> getCommodityRules(@NotNull String of, @NotNull List<String> args) {
        return commodityRulesLookup.getOrDefault(of.toLowerCase(), _ -> Optional.of(new GeneralRules())).apply(args);
    }
}
