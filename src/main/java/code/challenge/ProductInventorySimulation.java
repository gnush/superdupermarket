package code.challenge;

import code.challenge.datasource.StaticData;
import code.challenge.product.Product;

import java.time.LocalDate;

public class ProductInventorySimulation {
    public static void main(String[] args) {
        LocalDate simulationStartDate = LocalDate.now();
        int simulationDays = 10;

        var products = StaticData.getInstance().getProducts();
        System.out.println("Product inventory on " + simulationStartDate);
        products.forEach(System.out::println);

        for (int dayOffset=1; dayOffset <= simulationDays; dayOffset++) {
            var currentDate = simulationStartDate.plusDays(dayOffset);
            SimulationContext.setClock(currentDate);
            System.out.println("\nDay " + currentDate);

            products.forEach(Product::dailyUpdate); // age each product by one day
            //products = products.stream().filter(p -> !p.toRemove()).toList(); // remove products from inventory
            products.stream().map(Product::overview).forEach(System.out::println); // Print inventory overview
        }
    }
}
