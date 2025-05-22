package io.github.gnush.observer;

import io.github.gnush.commodity.Commodity;
import org.jetbrains.annotations.NotNull;

public class CommodityQualityObserver implements Observer<Commodity, CommodityChange> {
    @Override
    public void update(@NotNull Commodity commodity, @NotNull CommodityChange change) {
        StringBuilder msg = new StringBuilder("quality of '" + commodity.label);

        switch (change) {
            case CommodityChange.QualityIncrease inc -> {
                msg.append("' increased by ");
                msg.append(inc.change());
                System.out.println(msg);
            }
            case CommodityChange.QualityDecrease dec -> {
                msg.append("' decreased by ");
                msg.append(dec.change());
                System.out.println(msg);
            }
            case CommodityChange.DailyUpdate _ -> { }
        }
    }
}
