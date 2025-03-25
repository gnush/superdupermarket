package code.challenge.observer;

import code.challenge.product.Product;
import org.jetbrains.annotations.NotNull;

public class ProductQualityObserver implements Observer<Product, ProductChange> {
    @Override
    public void update(@NotNull Product product, @NotNull ProductChange change) {
        StringBuilder msg = new StringBuilder("quality of '" + product.label);

        switch (change) {
            case ProductChange.QualityIncrease inc -> {
                msg.append("' increased by ");
                msg.append(inc.change());
            }
            case ProductChange.QualityDecrease dec -> {
                msg.append("' decreased by ");
                msg.append(dec.change());
            }
            case ProductChange.DailyUpdate _ -> { }
        }

        System.out.println(msg);
    }
}
