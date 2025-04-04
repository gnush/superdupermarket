package code.challenge.datasource;

import code.challenge.commodity.Commodity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface DataSource {
    @NotNull List<Commodity> getCommodities();
}
