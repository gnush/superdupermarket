package code.challenge.observer;

import code.challenge.product.Product;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ProductInventoryObserver implements Observer<Product, ProductChange> {
    @NotNull private final List<Product> inventory;

    public ProductInventoryObserver(@NotNull List<Product> inventory) {
        this.inventory = inventory;
    }

    @Override
    public void update(@NotNull Product product, @NotNull ProductChange productChange) {
        switch (productChange) {
            case ProductChange.DailyUpdate _ ->  {
                if (inventory.contains(product) && product.toRemove()) {
                    System.out.printf("removing '%s' from inventory%n", product.label);
                    inventory.remove(product);
                }
            }
            case ProductChange.QualityIncrease inc ->
                    System.out.printf("quality of '%s' increased by %s%n", product.label, inc.change());
            case ProductChange.QualityDecrease dec ->
                    System.out.printf("quality of '%s' decreased by %s%n", product.label, dec.change());
        }
    }
}
