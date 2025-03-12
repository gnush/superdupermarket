package code.challenge.datasource;

import code.challenge.product.Product;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface DataSource {
    @NotNull List<Product> getProducts();
}
