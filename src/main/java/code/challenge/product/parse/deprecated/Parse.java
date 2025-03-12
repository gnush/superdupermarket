package code.challenge.product.parse.deprecated;

import code.challenge.currency.Currency;
import code.challenge.currency.EUR;
import code.challenge.currency.USD;
import code.challenge.product.deprecated.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public final class Parse {
    public static Optional<? extends NullableExpirationProduct> parseProduct(String TYPE, List<String> fields) {
        return switch (TYPE) {
            case "Cheese" -> parseCheese(fields);
            case "Wine" -> parseWine(fields);
            case "Bricks" -> parseBricks(fields);
            default -> {
                System.err.printf("Treating unrecognized product type \"%s\" as generic%n", TYPE);
                yield parseGeneric(fields);
            }
        };
    }

    public static Optional<? extends NullableExpirationProduct> parseCheese(List<String> fields) {
        if (fields.size() != 5)
            return Optional.empty();

        try {
            return Cheese.of(
                    fields.getFirst(),
                    parseCurrency(fields.get(1), fields.get(2)),
                    Integer.parseInt(fields.get(3)),
                    LocalDate.parse(fields.get(4))
            );
        } catch (Exception _) {
            return Optional.empty();
        }
    }

    public static Optional<? extends NullableExpirationProduct> parseWine(List<String> fields) {
        if (fields.size() != 4)
            return Optional.empty();

        try {
            return Wine.of(
                    fields.getFirst(),
                    parseCurrency(fields.get(1), fields.get(2)),
                    Integer.parseInt(fields.get(3))
            );
        } catch (Exception _) {
            return Optional.empty();
        }
    }

    public static Optional<? extends NullableExpirationProduct> parseGeneric(List<String> fields) {
        if (fields.size() < 4)
            return Optional.empty();

        try {
            LocalDate expirationDate = fields.size() > 4 ? LocalDate.parse(fields.get(4)) : null;
            return Optional.of(
                    new Generic(
                            fields.getFirst(),
                            parseCurrency(fields.get(1), fields.get(2)),
                            Integer.parseInt(fields.get(3)),
                            expirationDate
                    )
            );
        } catch (Exception _) {
            return Optional.empty();
        }
    }

    public static Optional<? extends NullableExpirationProduct> parseBricks(List<String> fields) {
        if (fields.size() != 5)
            return Optional.empty();

        try {
            return Bricks.of(
                    fields.get(1),
                    parseCurrency(fields.get(2), fields.get(3)),
                    Integer.parseInt(fields.get(4)),
                    LocalDate.parse(fields.getFirst())
            );
        } catch (Exception _) {
            return Optional.empty();
        }
    }

    private static Currency parseCurrency(String TYPE, String amount) {
        double _amount = Double.parseDouble(amount);

        return switch (TYPE.toUpperCase()) {
            case "EUR" -> new EUR(_amount);
            case "USD" -> new USD(_amount);
            default -> throw new IllegalArgumentException(TYPE + " is not supported");
        };
    }
}
