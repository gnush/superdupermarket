package code.challenge;

import code.challenge.datasource.CSVSource;
import code.challenge.datasource.SQLiteSource;
import code.challenge.datasource.StaticData;
import code.challenge.observer.ProductInventoryObserver;
import code.challenge.product.Product;
import code.challenge.product.ProductLookup;
import code.challenge.product.rule.BricksRules;
import code.challenge.product.rule.CheeseRules;
import code.challenge.product.rule.WineRules;
import code.challenge.util.CSVReader;

import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class ProductInventoryObserverSimulation {
    public static void main(String[] args) {
        LocalDate simulationStartDate = LocalDate.now();
        int simulationDays = 15;

        ProductLookup.register("Wine", _ -> Optional.of(new WineRules()));
        ProductLookup.register("Cheese", _ -> Optional.of(new CheeseRules()));
        ProductLookup.register("Bricks", BricksRules::parse);

        var source = args != null && args.length > 0 ? args[0] : "csv";
        var products = new CopyOnWriteArrayList<>(switch (source) {
            case "csv" -> new CSVSource(CSVReader.defaultReader()).getProducts();
            case "sql" -> SQLiteSource.defaultDatabase().getProducts();
            default -> StaticData.getInstance().getProducts();
        });

        System.out.println("Product inventory on " + simulationStartDate);
        products.forEach(System.out::println);

        var inventoryObserver = new ProductInventoryObserver(products);
        products.forEach(p -> p.attach(inventoryObserver));

        for (int dayOffset=1; dayOffset <= simulationDays; dayOffset++) {
            var currentDate = simulationStartDate.plusDays(dayOffset);
            SimulationContext.setClock(currentDate);
            System.out.println("\nDay " + currentDate);

            products.forEach(Product::dailyUpdate); // age each product by one day
        }

        System.out.println("\nProduct inventory on " + simulationStartDate.plusDays(simulationDays));
        products.forEach(System.out::println);
    }
}
