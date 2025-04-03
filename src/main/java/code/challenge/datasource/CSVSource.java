package code.challenge.datasource;

import code.challenge.util.CSVReader;
import code.challenge.product.NullableExpirationProduct;
import code.challenge.product.parse.Parse;

import java.util.*;
import java.util.stream.Stream;

public class CSVSource implements DataSource {
    private final CSVReader csv;

    public CSVSource(CSVReader csv) {
        this.csv = csv;
    }

    private Optional<? extends NullableExpirationProduct> parseCSVRow(List<String> cells) {
        if (cells.isEmpty())
            return Optional.empty();

        return Parse.parseProduct(cells.getFirst(), cells.subList(2, cells.size()));
    }

    @Override
    public List<NullableExpirationProduct> getProducts() {
        csv.readCSVContent();
        var csvContent = csv.getCSVRows();

        if (csvContent.isEmpty()) {
            return Collections.emptyList();
        } else {
            var rows = csvContent.get();
            Stream<NullableExpirationProduct> productsStream = rows.stream()
                    .map(this::parseCSVRow)
                    .filter(Optional::isPresent)
                    .map(Optional::get);
            return productsStream.toList();
        }
    }
}
