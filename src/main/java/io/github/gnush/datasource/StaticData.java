package io.github.gnush.datasource;

import io.github.gnush.SimulationContext;
import io.github.gnush.currency.EUR;
import io.github.gnush.currency.USD;
import io.github.gnush.commodity.ExpirationDate;
import io.github.gnush.commodity.Commodity;
import io.github.gnush.commodity.rule.CheeseRules;
import io.github.gnush.commodity.rule.GeneralRules;
import io.github.gnush.commodity.rule.WineRules;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Singleton class to provide example commodity data
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
    public @NotNull List<Commodity> getCommodities() {
        var l = List.of(
                Commodity.of("Stinker", new EUR(new BigDecimal("42")), 30, new ExpirationDate.ExpiresAt(LocalDate.now(SimulationContext.clock).plusDays(60)), new CheeseRules()),
                Commodity.of("Roter", new EUR(new BigDecimal("4.2")), 40, new WineRules()),
                Commodity.of("Weisser", new EUR(new BigDecimal("1")), 15, new WineRules()),
                Commodity.of("Riecht Streng", new EUR(new BigDecimal("4.4")), 45, new ExpirationDate.ExpiresAt(LocalDate.now(SimulationContext.clock).plusDays(75)), new CheeseRules()),
                Commodity.of("GenericStuff", new EUR(new BigDecimal("0.4")), 10, new ExpirationDate.ExpiresAt(LocalDate.now(SimulationContext.clock).plusDays(9)), new GeneralRules()),
                Commodity.of("Schlacke", new USD(new BigDecimal("2.4")), 5, new ExpirationDate.ExpiresAt(LocalDate.now(SimulationContext.clock).plusYears(2)), new GeneralRules())
        );

        Stream<Commodity> commodities = l.stream().filter(Optional::isPresent).map(Optional::get);
        return commodities.toList();
    }
}
