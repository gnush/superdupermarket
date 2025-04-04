package code.challenge;

import code.challenge.datasource.DataSource;
import code.challenge.product.Product;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

public class ProductInventorySimulation {
    public static void runSimulation(@NotNull DataSource source, @NotNull LocalDate simulationStartDate, int simulateDays, boolean removeProducts) {
        var products = source.getProducts();
        System.out.println("Product inventory on " + simulationStartDate);
        products.forEach(System.out::println);

        for (int dayOffset=1; dayOffset <= simulateDays; dayOffset++) {
            var currentDate = simulationStartDate.plusDays(dayOffset);
            SimulationContext.setClock(currentDate);
            System.out.println("\nDay " + currentDate);

            products.forEach(Product::dailyUpdate); // age each product by one day
            if (removeProducts)
                products = products.stream().filter(p -> !p.toRemove()).toList(); // remove products from inventory
            products.stream().map(Product::overview).forEach(System.out::println); // Print inventory overview
        }
    }
}
