package io.github.gnush.datasource;

import io.github.gnush.commodity.Commodity;
import io.github.gnush.commodity.parse.Parse;
import io.github.gnush.datasource.db.CommodityDao;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class HibernateSource implements DataSource {
    private final CommodityDao commodityDao = new CommodityDao();

    @Override
    public @NotNull List<Commodity> getCommodities() {
        return commodityDao.commodities().stream().map(Parse::fromEntity).filter(Optional::isPresent).map(Optional::get).toList();
    }
}
