package code.challenge.util;

import code.challenge.datasource.CSVSource;
import code.challenge.datasource.DataSource;
import code.challenge.datasource.SQLiteSource;
import code.challenge.datasource.StaticData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;

public class ArgumentParser {
    public final static int DEFAULT_SIMULATION_DAYS = 10;
    public final static DataSource DEFAULT_DATASOURCE = StaticData.getInstance();
    public final static Tuple2<Integer, DataSource> DEFAULT_ARGUMENTS = new Tuple2<>(
            DEFAULT_SIMULATION_DAYS,
            DEFAULT_DATASOURCE
    );

    /**
     * Parses command line arguments
     * @param args command line arguments passed on execution
     * @return A tuple consisting of the number of simulation days (int) and the DataSource to use.
     */
    public static Tuple2<Integer, DataSource> parseArguments(String[] args) {
        if (args == null
                || Arrays.stream(args).filter(Objects::isNull).toList().isEmpty())
            return DEFAULT_ARGUMENTS;

        return switch (args.length) {
            case 1  -> new Tuple2<>(
                    parseDays(args[0]),
                    StaticData.getInstance()
            );
            case 2  -> new Tuple2<>(
                    parseDays(args[0]),
                    parseDatasource(args[1])
            );
            case 3  -> new Tuple2<>(
                    parseDays(args[0]),
                    parseDatasource(args[1], args[2])
            );
            default -> DEFAULT_ARGUMENTS;
        };
    }

    /**
     * Parses a DataSource from command line arguments
     * @param sourceType The sourceType of the DataSource to parse (csv or sqlite)
     * @param path The path to the csv source file or sqlite db.
     * @param csvDelimiter The delimiter the csv file uses to separate its cells
     * @return The DataSource parsed from the arguments or the default StaticData
     */
    public static DataSource parseDataSourceArgs(@NotNull String sourceType, @Nullable Path path, @Nullable String csvDelimiter) {
        if (path == null)
            return parseDatasource(sourceType);

        return parseDatasourceFromPath(path, csvDelimiter);
    }

    private static int parseDays(@NotNull String days) {
        try {
            return Integer.parseInt(days);
        } catch (Exception _) {
            return DEFAULT_ARGUMENTS._1();
        }
    }

    private static @NotNull DataSource parseDatasource(@NotNull String type) {
        return switch (type.toLowerCase()) {
            case "csv" -> new CSVSource(CSVReader.defaultReader());
            case "sqlite" -> SQLiteSource.defaultDatabase();
            default -> DEFAULT_ARGUMENTS._2();
        };
    }

    private static @NotNull DataSource parseDatasource(@NotNull String type, @NotNull String file) {
        Path path = Path.of(file);

        return switch (type.toLowerCase()) {
            case "csv" -> new CSVSource(new CSVReader(path, ";"));
            case "sqlite" -> new SQLiteSource(path);
            default -> DEFAULT_ARGUMENTS._2();
        };
    }

    private static @NotNull DataSource parseDatasourceFromPath(@NotNull Path path, @Nullable String csvDelimiter) {
        if (FileSystems.getDefault().getPathMatcher("glob:*.csv").matches(path.getFileName())) {
            if (csvDelimiter != null)
                return new CSVSource(new CSVReader(path, csvDelimiter));
            System.err.println("no delimiter supplied for csv file");
        } else if (FileSystems.getDefault().getPathMatcher("glob:*.sqlite").matches(path.getFileName())) {
            return new SQLiteSource(path);
        }

        System.err.printf("Could not parse %s, falling back to static data%n", path);
        return DEFAULT_ARGUMENTS._2();
    }
}
