package code.challenge;

import code.challenge.datasource.deprecated.StaticData;
import code.challenge.product.deprecated.NullableExpirationProduct;

import java.time.LocalDate;

public class DeprecatedProductInventorySimulation {

    public static void main(String[] args) {
        var products = StaticData.getInstance().getProducts();

        System.out.println("Product inventory");
        products.forEach(System.out::println);

        LocalDate simulationStartDate = LocalDate.now();
        int simulationDays = 10;

        for (int dayOffset=1; dayOffset <= simulationDays; dayOffset++) {
            var currentDate = simulationStartDate.plusDays(dayOffset);
            SimulationContext.setClock(currentDate);
            System.out.println("\nDay " + currentDate);

            products.forEach(NullableExpirationProduct::dailyUpdate); // age each product by one day
            //products = products.stream().filter(p -> !p.toRemove()).toList(); // remove products from inventory
            products.stream().map(NullableExpirationProduct::overview).forEach(System.out::println); // Print inventory overview
        }
    }
}
