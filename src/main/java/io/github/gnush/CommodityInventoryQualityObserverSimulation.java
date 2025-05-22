package io.github.gnush;

import io.github.gnush.datasource.DataSource;
import io.github.gnush.observer.CommodityQualityObserver;
import io.github.gnush.commodity.Commodity;
import io.github.gnush.commodity.CommodityLookup;
import io.github.gnush.commodity.rule.BricksRules;
import io.github.gnush.commodity.rule.CheeseRules;
import io.github.gnush.commodity.rule.WineRules;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.Optional;

public class CommodityInventoryQualityObserverSimulation {
    public static void runSimulation(@NotNull DataSource source, @NotNull LocalDate simulationStartDate, int simulateDays, boolean removeCommodities) {
        CommodityLookup.register("Wine", _ -> Optional.of(new WineRules()));
        CommodityLookup.register("Cheese", _ -> Optional.of(new CheeseRules()));
        CommodityLookup.register("Bricks", BricksRules::parse);

        var commodities = source.getCommodities();

        System.out.println("Commodity inventory on " + simulationStartDate);
        commodities.forEach(System.out::println);

        var inventoryObserver = new CommodityQualityObserver();
        commodities.forEach(c -> c.attach(inventoryObserver));

        for (int dayOffset=1; dayOffset <= simulateDays; dayOffset++) {
            var currentDate = simulationStartDate.plusDays(dayOffset);
            SimulationContext.setClock(currentDate);
            System.out.println("\nDay " + currentDate);

            commodities.forEach(Commodity::dailyUpdate); // age each commodity by one day

            if (removeCommodities)
                commodities = commodities.stream().filter(p -> !p.toRemove()).toList(); // remove commodities from inventory
        }

        System.out.println("\nCommodity inventory on " + simulationStartDate.plusDays(simulateDays));
        commodities.forEach(System.out::println);
    }
}
