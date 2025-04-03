package code.challenge;

import code.challenge.datasource.DataSource;
import code.challenge.product.Product;
import code.challenge.util.ArgumentParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import picocli.CommandLine;

import java.nio.file.Path;
import java.time.LocalDate;

@CommandLine.Command(name = "Product Inventory Simulation", version = "1", mixinStandardHelpOptions = true)
public class ProductInventorySimulation implements Runnable {
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

        var products = source.getProducts();
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

    public static void main(String[] args) {
        int exitCode = new CommandLine(new ProductInventorySimulation()).execute(args);
        System.exit(exitCode);
    }
}
