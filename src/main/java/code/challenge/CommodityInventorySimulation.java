package code.challenge;

import code.challenge.datasource.DataSource;
import code.challenge.commodity.Commodity;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

public class CommodityInventorySimulation {
    public static void runSimulation(@NotNull DataSource source, @NotNull LocalDate simulationStartDate, int simulateDays, boolean removeCommodities) {
        var commodities = source.getCommodities();
        System.out.println("Commodity inventory on " + simulationStartDate);
        commodities.forEach(System.out::println);

        for (int dayOffset=1; dayOffset <= simulateDays; dayOffset++) {
            var currentDate = simulationStartDate.plusDays(dayOffset);
            SimulationContext.setClock(currentDate);
            System.out.println("\nDay " + currentDate);

            commodities.forEach(Commodity::dailyUpdate); // age each commodity by one day
            if (removeCommodities)
                commodities = commodities.stream().filter(p -> !p.toRemove()).toList(); // remove commodities from inventory
            commodities.stream().map(Commodity::overview).forEach(System.out::println); // Print inventory overview
        }
    }
}
