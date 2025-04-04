package code.challenge.observer;

import code.challenge.commodity.Commodity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommodityInventoryObserver implements Observer<Commodity, CommodityChange> {
    @NotNull private final List<Commodity> inventory;

    public CommodityInventoryObserver(@NotNull List<Commodity> inventory) {
        this.inventory = inventory;
    }

    @Override
    public void update(@NotNull Commodity commodity, @NotNull CommodityChange commodityChange) {
        switch (commodityChange) {
            case CommodityChange.DailyUpdate _ ->  {
                if (inventory.contains(commodity) && commodity.toRemove()) {
                    System.out.printf("removing '%s' from inventory%n", commodity.label);
                    inventory.remove(commodity);
                }
            }
            case CommodityChange.QualityIncrease inc ->
                    System.out.printf("quality of '%s' increased by %s%n", commodity.label, inc.change());
            case CommodityChange.QualityDecrease dec ->
                    System.out.printf("quality of '%s' decreased by %s%n", commodity.label, dec.change());
        }
    }
}
