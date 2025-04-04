package code.challenge.observer;

public sealed interface CommodityChange permits CommodityChange.QualityIncrease, CommodityChange.QualityDecrease, CommodityChange.DailyUpdate {
    record QualityIncrease(int change) implements CommodityChange { }
    record QualityDecrease(int change) implements CommodityChange { }
    final class DailyUpdate implements CommodityChange {
        private DailyUpdate() {}

        private static DailyUpdate INSTANCE;

        public static DailyUpdate getInstance() {
            if (INSTANCE == null) {
                INSTANCE = new DailyUpdate();
            }

            return INSTANCE;
        }
    }
}
