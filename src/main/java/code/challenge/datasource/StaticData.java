package code.challenge.datasource;

import code.challenge.SimulationContext;
import code.challenge.currency.EUR;
import code.challenge.currency.USD;
import code.challenge.product.ExpirationDate;
import code.challenge.product.Product;
import code.challenge.product.rule.CheeseRules;
import code.challenge.product.rule.GeneralRules;
import code.challenge.product.rule.WineRules;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Singleton class to provide example product data
 */
public final class StaticData implements DataSource {
    private StaticData() {}

    private static StaticData INSTANCE;

    public static @NotNull StaticData getInstance() {
        if (INSTANCE == null)
            INSTANCE = new StaticData();

        return INSTANCE;
    }

    @Override
    public @NotNull List<Product> getProducts() {
        var l = List.of(
                Product.of("Stinker", new EUR(42), 30, new ExpirationDate.ExpiresAt(LocalDate.now(SimulationContext.clock).plusDays(60)), new CheeseRules()),
                Product.of("Roter", new EUR(4.2), 40, new WineRules()),
                Product.of("Weisser", new EUR(1), 15, new WineRules()),
                Product.of("Riecht Streng", new EUR(4.4), 45, new ExpirationDate.ExpiresAt(LocalDate.now(SimulationContext.clock).plusDays(75)), new CheeseRules()),
                Product.of("GenericStuff", new EUR(0.4), 10, new ExpirationDate.ExpiresAt(LocalDate.now(SimulationContext.clock).plusDays(9)), new GeneralRules()),
                Product.of("Schlacke", new USD(2.4), 5, new ExpirationDate.ExpiresAt(LocalDate.now(SimulationContext.clock).plusYears(2)), new GeneralRules())
        );

        Stream<Product> products = l.stream().filter(Optional::isPresent).map(Optional::get);
        return products.toList();
    }
}
