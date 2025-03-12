package code.challenge.observer;

import code.challenge.product.Product;
import org.jetbrains.annotations.NotNull;

public class ProductQualityObserver implements Observer<Product, ProductQualityChange> {
    @Override
    public void update(@NotNull Product product, @NotNull ProductQualityChange change) {
        StringBuilder msg = new StringBuilder("quality of '" + product.label);

        switch (change) {
            case ProductQualityChange.Increase inc -> {
                msg.append("' increased by ");
                msg.append(inc.change());
            }
            case ProductQualityChange.Decrease dec -> {
                msg.append("' decreased by ");
                msg.append(dec.change());
            }
        }

        System.out.println(msg);
    }
}
