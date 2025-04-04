package code.challenge.datasource;

import code.challenge.commodity.Commodity;
import code.challenge.commodity.parse.Parse;
import code.challenge.util.CSVReader;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CSVSource implements DataSource {
    private final CSVReader csv;

    public CSVSource(CSVReader csv) {
        this.csv = csv;
    }

    private @NotNull Optional<Commodity> parseCSVRow(@NotNull List<String> cells) {
        if (cells.size() <= 2)
            return Optional.empty();

        try {
            final int numConfigCells = 2;
            int numRuleArgs = Integer.parseInt(cells.get(1));

            return Parse.commodity(
                    cells.getFirst(),
                    cells.subList(numConfigCells, numConfigCells+numRuleArgs),
                    cells.subList(numConfigCells+numRuleArgs, cells.size())
            );
        } catch (NumberFormatException _) {
            System.err.printf("%s: expected natural number, but got %s%n", cells.getFirst(), cells.get(1));
            return Optional.empty();
        }
    }

    @Override
    public @NotNull List<Commodity> getCommodities() {
        csv.readCSVContent();
        var csvContent = csv.getCSVRows();

        if (csvContent.isEmpty()) {
            return Collections.emptyList();
        } else {
            var rows = csvContent.get();
            return rows.stream()
                    .map(this::parseCSVRow)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();
        }
    }
}