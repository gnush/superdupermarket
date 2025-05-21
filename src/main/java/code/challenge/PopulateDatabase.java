package code.challenge;

import code.challenge.commodity.ExpirationDate;
import code.challenge.currency.EUR;
import code.challenge.currency.USD;
import code.challenge.datasource.db.CommodityDao;
import code.challenge.datasource.db.CommodityEntity;

import java.util.List;
import java.util.logging.Level;

public class PopulateDatabase {
    public static void main(String[] args) {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);

        var commodityDao = new CommodityDao();
        var commodities = List.of(
                new CommodityEntity("Cheese", "Stinker", new EUR(42), 30, new ExpirationDate.ExpiresAt(SimulationContext.now().plusDays(60))),
                new CommodityEntity("Wine", "Roter", new EUR(4.2), 40, ExpirationDate.DoesNotExpire.instance()),
                new CommodityEntity("Wine", "Weisser", new EUR(1), 15, ExpirationDate.DoesNotExpire.instance()),
                new CommodityEntity("Cheese", "Riecht Streng", new EUR(4.4), 45, new ExpirationDate.ExpiresAt(SimulationContext.now().plusDays(75))),
                new CommodityEntity("Generic", "GenericStuff", new EUR(0.4), 10, new ExpirationDate.ExpiresAt(SimulationContext.now().plusDays(9))),
                new CommodityEntity("Waste", "Schlacke", new USD(2.4), 5, new ExpirationDate.ExpiresAt(SimulationContext.now().plusYears(2)))
        );

        if (args != null && args.length > 0 && args[0].equals("fresh"))
            commodityDao.deleteAll();
        
        commodities.forEach(commodityDao::save);
    }
}
