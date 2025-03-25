package code.challenge.observer;

public sealed interface ProductChange permits ProductChange.QualityIncrease, ProductChange.QualityDecrease, ProductChange.DailyUpdate {
    record QualityIncrease(int change) implements ProductChange { }
    record QualityDecrease(int change) implements ProductChange { }
    final class DailyUpdate implements ProductChange {
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
