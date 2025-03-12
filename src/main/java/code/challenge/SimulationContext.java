package code.challenge;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class SimulationContext {
    private static final String FIXED_TIME = "T00:00:00.000Z";

    public static Clock clock = clock(LocalDate.now());

    private static Clock clock(LocalDate day) {
        return Clock.fixed(Instant.parse(day+FIXED_TIME), ZoneId.systemDefault());
    }

    public static void setClock(LocalDate day) {
        clock = clock(day);
    }
}
