package io.github.gnush;

import io.github.gnush.commodity.ExpirationDate;
import io.github.gnush.currency.EUR;
import io.github.gnush.currency.USD;
import io.github.gnush.datasource.db.CommodityDao;
import io.github.gnush.datasource.db.CommodityEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;

public class PopulateDatabase {
    public static void main(String[] args) {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);

        var commodityDao = new CommodityDao();
        var commodities = List.of(
                new CommodityEntity("Cheese", "Stinker", new EUR(new BigDecimal("42")), 30, new ExpirationDate.ExpiresAt(SimulationContext.now().plusDays(60))),
                new CommodityEntity("Wine", "Roter", new EUR(new BigDecimal("4.2")), 40, ExpirationDate.DoesNotExpire.instance()),
                new CommodityEntity("Wine", "Weisser", new EUR(new BigDecimal("1")), 15, ExpirationDate.DoesNotExpire.instance()),
                new CommodityEntity("Cheese", "Riecht Streng", new EUR(new BigDecimal("4.4")), 45, new ExpirationDate.ExpiresAt(SimulationContext.now().plusDays(75))),
                new CommodityEntity("Generic", "GenericStuff", new EUR(new BigDecimal("0.4")), 10, new ExpirationDate.ExpiresAt(SimulationContext.now().plusDays(9))),
                new CommodityEntity("Waste", "Schlacke", new USD(new BigDecimal("2.4")), 5, new ExpirationDate.ExpiresAt(SimulationContext.now().plusYears(2)))
        );

        if (args != null && args.length > 0 && args[0].equals("fresh"))
            commodityDao.deleteAll();
        
        commodities.forEach(commodityDao::save);
    }
}
