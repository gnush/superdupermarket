package code.challenge.util;

import code.challenge.datasource.CSVSource;
import code.challenge.datasource.DataSource;
import code.challenge.datasource.SQLiteSource;
import code.challenge.datasource.StaticData;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;

public class ArgumentParser {
    public static Tuple2<Integer, DataSource> DEFAULT_ARGUMENTS = new Tuple2<>(
            10,
            StaticData.getInstance()
    );

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
}
