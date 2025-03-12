package code.challenge.datasource;

import code.challenge.product.Product;
import code.challenge.product.parse.Parse;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SQLiteSource implements DataSource {
    private final @NotNull Path SQL_PATH;

    public SQLiteSource(@NotNull Path path) {
        this.SQL_PATH = path;
    }

    @Override
    public @NotNull List<Product> getProducts() {
        List<Product> products = new ArrayList<>();

        try (
                Connection connection = DriverManager.getConnection("jdbc:sqlite:" + SQL_PATH);
                Statement statement = connection.createStatement()
        ) {
            ResultSet res = statement.executeQuery("select * from products");

            while (res.next()) {
                var productType = res.getString("product_type");
                var productLabel = res.getString("label");
                var productQuality = res.getString("quality");
                var currencyType = res.getString("currency_type");
                var currencyAmount = res.getString("currency_amount");
                var expirationDate = res.getString("expiration_date");

                var rule_args = parseRuleArgs(res.getString("rule_args"));

                var product_args = expirationDate.isEmpty()
                        ? List.of(productLabel, currencyType, currencyAmount, productQuality)
                        : List.of(productLabel, currencyType, currencyAmount, productQuality, expirationDate);

                Parse.product(productType, rule_args, product_args)
                        .ifPresent(products::add);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return products;
    }

    private @NotNull List<String> parseRuleArgs(@NotNull String ruleArgs) {
        if (ruleArgs.length() < 2 || !ruleArgs.startsWith("[") || !ruleArgs.endsWith("]"))
            return Collections.emptyList();

        List<String> result = new ArrayList<>();
        var first = ruleArgs.indexOf("\"");
        var second = ruleArgs.indexOf("\"", first+1);

        while (first >= 0 && first < second) {
            result.add(ruleArgs.substring(first+1, second));

            first = ruleArgs.indexOf("\"", second+1);
            second = ruleArgs.indexOf("\"", first+1);
        }

        return result;
    }

    public static @NotNull SQLiteSource defaultDatabase() {
        return new SQLiteSource(Path.of("src","main","resources", "products.sqlite3"));
    }
}
