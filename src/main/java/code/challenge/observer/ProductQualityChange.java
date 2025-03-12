package code.challenge.observer;

public sealed interface ProductQualityChange permits ProductQualityChange.Increase, ProductQualityChange.Decrease {
    record Increase(int change) implements ProductQualityChange { }
    record Decrease(int change) implements ProductQualityChange { }
}
