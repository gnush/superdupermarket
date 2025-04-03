package code.challenge;

import code.challenge.product.Product;
import code.challenge.util.ArgumentParser;

import java.time.LocalDate;

public class ProductInventorySimulation {
    public static void main(String[] args) {
        final var arguments = ArgumentParser.parseArguments(args);
        final int simulationDays = arguments._1();
        final var datasource = arguments._2();

        LocalDate simulationStartDate = LocalDate.now();

        var products = datasource.getProducts();
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
