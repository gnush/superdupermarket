package code.challenge.util;

import code.challenge.datasource.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.FileSystems;
import java.nio.file.Path;

public class ArgumentParser {
    public final static int DEFAULT_SIMULATION_DAYS = 10;
    public final static DataSource DEFAULT_DATASOURCE = StaticData.getInstance();

    /**
     * Parses a DataSource from command line arguments
     * @param sourceType The sourceType of the DataSource to parse (csv or sqlite)
     * @param path The path to the csv source file or sqlite db.
     * @param csvDelimiter The delimiter the csv file uses to separate its cells
     * @return The DataSource parsed from the arguments or the default StaticData
     */
    public static DataSource parseDataSourceArgs(@NotNull String sourceType, @Nullable Path path, @Nullable String csvDelimiter) {
        return switch (sourceType.toLowerCase()) {
            case "csv" -> parseCSVDataSource(path, csvDelimiter);
            case "sqlite" -> parseSqliteDataSource(path);
            case "hibernate" -> new HibernateSource();
            default -> DEFAULT_DATASOURCE;
        };
    }

    private static @NotNull DataSource parseCSVDataSource(@Nullable Path path, @Nullable String csvDelimiter) {
        if (path == null)
            return new CSVSource(CSVReader.defaultReader());

        if (FileSystems.getDefault().getPathMatcher("glob:*.csv").matches(path.getFileName())) {
            if (csvDelimiter != null)
                return new CSVSource(new CSVReader(path, csvDelimiter));
            System.err.println("no delimiter supplied for csv file");
        }

        return fallbackDataSource(path);
    }

    private static @NotNull DataSource parseSqliteDataSource(@Nullable Path path) {
        if (path == null)
            return SQLiteSource.defaultDatabase();

        if (FileSystems.getDefault().getPathMatcher("glob:*.sqlite").matches(path.getFileName())) {
            return new SQLiteSource(path);
        }

        return fallbackDataSource(path);
    }

    private static @NotNull DataSource fallbackDataSource(@NotNull Path path) {
        System.err.printf("Could not parse %s, falling back to static data%n", path);
        return DEFAULT_DATASOURCE;
    }
}
