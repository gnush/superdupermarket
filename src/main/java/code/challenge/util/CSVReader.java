package code.challenge.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class CSVReader {
    @NotNull private final Path CSV_PATH;
    @NotNull private final String DELIMITER;

    @Nullable private List<List<String>> csvRows;

    public CSVReader(@NotNull Path path, @NotNull String delimiter) {
        CSV_PATH = path;
        DELIMITER = delimiter;
    }

    public void readCSVContent() {
        try(Stream<String> lines = Files.lines(CSV_PATH)) {
            csvRows = lines.map(line -> Arrays.stream(line.split(DELIMITER)).toList()).toList();
        } catch (NoSuchFileException e) {
            System.err.printf("'%s' does not exist%n", e.getMessage());
        } catch (IOException _) {
            System.err.printf("Error while reading '%s'%n", CSV_PATH);
        }
    }

    public @NotNull Optional<List<List<String>>> getCSVRows() {
        if (csvRows == null)
            return Optional.empty();

        return Optional.of(csvRows);
    }

    public static @NotNull CSVReader defaultReader() {
        return new CSVReader(Path.of("src","main","resources", "commodities.csv"), ";");
    }
}
