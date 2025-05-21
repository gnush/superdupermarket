package code.challenge.commodity.parse;

import code.challenge.currency.Currency;
import code.challenge.currency.EUR;
import code.challenge.currency.USD;
import code.challenge.commodity.ExpirationDate;
import code.challenge.commodity.Commodity;
import code.challenge.commodity.CommodityLookup;
import code.challenge.datasource.db.CommodityEntity;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public final class Parse {
    @NotNull
    public static Optional<Commodity> commodity(@NotNull String type, @NotNull List<String> ruleArgs, @NotNull List<String> commodityArgs) {
        try {
            ExpirationDate expirationDate = commodityArgs.size() > 4
                    ? new ExpirationDate.ExpiresAt(LocalDate.parse(commodityArgs.get(4)))
                    : ExpirationDate.DoesNotExpire.instance();
            return Commodity.of(
                    commodityArgs.getFirst(),
                    parseCurrency(commodityArgs.get(1), commodityArgs.get(2)),
                    Integer.parseInt(commodityArgs.get(3)),
                    expirationDate,
                    CommodityLookup.getCommodityRules(type, ruleArgs).get()
            );
        } catch (Exception _) {
            return Optional.empty();
        }
    }

    @NotNull
    public static Optional<Commodity> fromEntity(CommodityEntity entity) {
        try {
            return Commodity.of(
                    entity.getLabel(),
                    parseCurrency(entity.getCurrency(), entity.getBasePrice()),
                    entity.getQuality(),
                    entity.getExpirationDate(),
                    CommodityLookup.getCommodityRules(
                            entity.getCategory().getName(),
                            entity.getExtraAttributes()
                    ).get()
            );
        } catch (Exception _) {
            return Optional.empty();
        }
    }

    @NotNull
    private static Currency parseCurrency(@NotNull String TYPE, @NotNull String amount) throws NumberFormatException {
        BigDecimal _amount = new BigDecimal(amount);

        return switch (TYPE.toUpperCase()) {
            case "EUR" -> new EUR(_amount);
            case "USD" -> new USD(_amount);
            default -> throw new IllegalArgumentException("currency '" + TYPE + "' is not supported");
        };
    }

    @NotNull
    private static Currency parseCurrency(@NotNull String TYPE, @NotNull BigDecimal amount) {
        return switch (TYPE.toUpperCase()) {
            case "EUR" -> new EUR(amount);
            case "USD" -> new USD(amount);
            default -> throw new IllegalArgumentException("currency '" + TYPE + "' is not supported");
        };
    }
}
