package io.github.gnush.commodity;

import io.github.gnush.SimulationContext;

import java.time.LocalDate;

public class CommodityTestUtil {
    public static void performDailyUpdateAndAdvanceClockByOne(Commodity commodity) {
        SimulationContext.setClock(LocalDate.now(SimulationContext.clock).plusDays(1));
        commodity.dailyUpdate();
    }
}
