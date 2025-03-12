package code.challenge.datasource.deprecated;

import code.challenge.product.deprecated.NullableExpirationProduct;

import java.util.List;

public interface DataSource {
    List<NullableExpirationProduct> getProducts();
}
