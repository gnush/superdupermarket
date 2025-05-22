package io.github.gnush.datasource;

import io.github.gnush.commodity.Commodity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface DataSource {
    @NotNull List<Commodity> getCommodities();
}
