package code.challenge.datasource;

import code.challenge.currency.EUR;
import code.challenge.currency.USD;
import code.challenge.product.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Singleton class to provide example product data
 */
public final class StaticData implements DataSource {
    private StaticData() {}

    private static StaticData INSTANCE;

    public static StaticData getInstance() {
        if (INSTANCE == null)
            INSTANCE = new StaticData();

        return INSTANCE;
    }

    @Override
    public List<NullableExpirationProduct> getProducts() {
        List<Optional<? extends NullableExpirationProduct>> l = List.of(
                Cheese.of("Stinker", new EUR(42), 30, LocalDate.now().plusDays(60)),
                Wine.of("Roter", new EUR(4.2), 33),
                Wine.of("Weisser", new EUR(1), 15),
                Cheese.of("Riecht Streng", new EUR(4.4), 45, LocalDate.now().plusDays(75)),
                Optional.of(new Generic("GenericStuff", new EUR(0.4), 10, LocalDate.now().plusDays(9))),
                Optional.of(new Generic("Schlacke", new USD(2.4), 5, LocalDate.now().plusYears(2))),
                Bricks.of("???", new EUR(14.95), 100, LocalDate.now().minusMonths(1))
        );

        Stream<NullableExpirationProduct> products = l.stream().filter(Optional::isPresent).map(Optional::get);
        return products.toList();
    }
}