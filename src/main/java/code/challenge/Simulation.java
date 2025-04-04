package code.challenge;

import code.challenge.datasource.DataSource;
import code.challenge.util.ArgumentParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import picocli.CommandLine;

import java.nio.file.Path;
import java.time.LocalDate;

@CommandLine.Command(name = "Product Inventory Simulation", version = "1.0", mixinStandardHelpOptions = true)
public class Simulation implements Runnable {
    private final static String DEFAULT_VALUE = "@|underline ${DEFAULT-VALUE}|@";
    private final static String DATE_FORMAT = "@|underline YYYY-MM-DD|@";

    @CommandLine.Option(names = { "-n", "--days" }, paramLabel = "NUMBER",
            defaultValue = ""+ ArgumentParser.DEFAULT_SIMULATION_DAYS,
            description = "Number of days to simulate (default: "+DEFAULT_VALUE+")")
    int simulationDays = ArgumentParser.DEFAULT_SIMULATION_DAYS;

    @CommandLine.Option(names = { "-c", "--clean" }, description = "Remove insufficient products at the end of each simulation day")
    boolean removeProductsRequested;

    @CommandLine.Option(names = { "-d", "--startDay" }, paramLabel = "DATE",
                        description = "Date to start the simulation on. @|underline DATE|@ format is "+DATE_FORMAT+" (default: @|underline today|@)")
    LocalDate simulationStartDate = LocalDate.now();

    @CommandLine.Option(names = { "-s", "--source" }, paramLabel = "TYPE", defaultValue = "static",
            description = "Source type to use. @|underline TYPE|@ can be static, csv, sqlite. (default: "+DEFAULT_VALUE+"). Option is ignored when @|underline -f|@ is set")
    @NotNull String sourceType = "static";

    @CommandLine.Option(names = { "-f", "--file" }, paramLabel = "FILE", description = "The csv file or sqlite database to use")
    @Nullable Path path = null;

    @CommandLine.Option(names = { "--delimiter" }, paramLabel = "DEL",
            description = "The cell delimiter of the csv file. Use in combination with -f=FILE@|underline .csv|@")
    @Nullable String csvDelimiter = null;

    @CommandLine.Option(names = { "-e", "--exec" }, paramLabel = "SIM", defaultValue = "inventory",
                        description = "The simulation to run. @|underline SIM|@ can be inventory, quality, autoremove. (default: "+DEFAULT_VALUE+")")
    @NotNull String simulation = "inventory";

    @Override
    public void run() {
        DataSource source = ArgumentParser.parseDataSourceArgs(sourceType, path, csvDelimiter);
        SimulationContext.setClock(simulationStartDate);

        switch (simulation.toLowerCase()) {
            case "quality" -> ProductInventoryQualityObserverSimulation.runSimulation(
                    source,
                    simulationStartDate,
                    simulationDays,
                    removeProductsRequested
            );
            case "autoremove" -> ProductInventoryObserverSimulation.runSimulation(
                    source,
                    simulationStartDate,
                    simulationDays
            );
            default -> ProductInventorySimulation.runSimulation(
                    source,
                    simulationStartDate,
                    simulationDays,
                    removeProductsRequested
            );
        }
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Simulation()).execute(args);
        System.exit(exitCode);
    }
}
