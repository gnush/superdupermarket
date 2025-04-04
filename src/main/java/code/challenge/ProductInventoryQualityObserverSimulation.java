package code.challenge;

import code.challenge.datasource.DataSource;
import code.challenge.observer.ProductQualityObserver;
import code.challenge.product.Product;
import code.challenge.product.ProductLookup;
import code.challenge.product.rule.BricksRules;
import code.challenge.product.rule.CheeseRules;
import code.challenge.product.rule.WineRules;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.Optional;

public class ProductInventoryQualityObserverSimulation {
    public static void runSimulation(@NotNull DataSource source, @NotNull LocalDate simulationStartDate, int simulateDays, boolean removeProducts) {
        ProductLookup.register("Wine", _ -> Optional.of(new WineRules()));
        ProductLookup.register("Cheese", _ -> Optional.of(new CheeseRules()));
        ProductLookup.register("Bricks", BricksRules::parse);

        var products = source.getProducts();

        System.out.println("Product inventory on " + simulationStartDate);
        products.forEach(System.out::println);

        var inventoryObserver = new ProductQualityObserver();
        products.forEach(p -> p.attach(inventoryObserver));

        for (int dayOffset=1; dayOffset <= simulateDays; dayOffset++) {
            var currentDate = simulationStartDate.plusDays(dayOffset);
            SimulationContext.setClock(currentDate);
            System.out.println("\nDay " + currentDate);

            products.forEach(Product::dailyUpdate); // age each product by one day

            if (removeProducts)
                products = products.stream().filter(p -> !p.toRemove()).toList(); // remove products from inventory
        }

        System.out.println("\nProduct inventory on " + simulationStartDate.plusDays(simulateDays));
        products.forEach(System.out::println);
    }
}
