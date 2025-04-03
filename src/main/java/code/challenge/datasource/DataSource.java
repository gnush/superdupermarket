package code.challenge.datasource;

import code.challenge.product.NullableExpirationProduct;

import java.util.List;

public interface DataSource {
    List<NullableExpirationProduct> getProducts();
}
