package code.challenge;

import code.challenge.datasource.DataSource;
import code.challenge.observer.ProductInventoryObserver;
import code.challenge.product.Product;
import code.challenge.product.ProductLookup;
import code.challenge.product.rule.BricksRules;
import code.challenge.product.rule.CheeseRules;
import code.challenge.product.rule.WineRules;
import code.challenge.util.ArgumentParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import picocli.CommandLine;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;


@CommandLine.Command(name = "Product Inventory Simulation", version = "1", mixinStandardHelpOptions = true)
public class ProductInventoryObserverSimulation implements Runnable {
    @CommandLine.Option(names = { "-n", "--days" }, description = "Number of days to simulate")
    int simulationDays = ArgumentParser.DEFAULT_SIMULATION_DAYS;

    @CommandLine.Option(names = { "-s", "--source" }, description = "Datasource of the inventory. Values: static, csv, sqlite")
    @NotNull String sourceType = "";

    @CommandLine.Option(names = { "-f", "--file" }, description = "The csv file or sqlite database to use")
    @Nullable Path path = null;

    @CommandLine.Option(names = { "-d", "--delimiter" }, description = "The delimiter of the csv file")
    @Nullable String csvDelimiter = null;

    @Override
    public void run() {
        DataSource source = ArgumentParser.parseDataSourceArgs(sourceType, path, csvDelimiter);

        LocalDate simulationStartDate = LocalDate.now();

        ProductLookup.register("Wine", _ -> Optional.of(new WineRules()));
        ProductLookup.register("Cheese", _ -> Optional.of(new CheeseRules()));
        ProductLookup.register("Bricks", BricksRules::parse);

        var products = new CopyOnWriteArrayList<>(source.getProducts());

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

    public static void main(String[] args) {
        int exitCode = new CommandLine(new ProductInventoryObserverSimulation()).execute(args);
        System.exit(exitCode);
    }
}
