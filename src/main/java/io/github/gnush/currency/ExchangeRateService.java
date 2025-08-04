package io.github.gnush.currency;

import org.jetbrains.annotations.NotNull;

import javax.json.Json;
import javax.json.JsonReader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

public class ExchangeRateService {
    @NotNull
    private static final HashMap<LocalDate, HashMap<String, HashMap<String, BigDecimal>>> cache = new HashMap<>();

    /**
     *
     * @param onDay The day for which the exchange rate should be determined.
     * @param from The ISO currency code of the currency to exchange from
     * @param to The ISO currency code to exchange into
     * @return The exchange rate of 1 unit of `from` to `to`
     */
    public static @NotNull Optional<BigDecimal> exchangeRate(@NotNull LocalDate onDay, @NotNull String from, @NotNull String to) {
        var lookup = cacheLookup(onDay, from, List.of(to)).orElseGet(() -> webLookup(onDay, from, List.of(to)));

        if (lookup.containsKey(to))
            return Optional.of(lookup.get(to));
        else
            return Optional.empty();
    }

    /**
     *
     * @param onDay The day for which the exchange rate should be determined.
     * @param from The ISO currency code of the currency to exchange from
     * @param to The ISO currency codes to exchange into
     * @return The exchange rates of 1 unit of `from` to `to`
     */
    public static @NotNull Map<String, BigDecimal> exchangeRates(@NotNull LocalDate onDay, @NotNull String from, @NotNull List<String> to) {
        return cacheLookup(onDay, from, to)
                .orElseGet(() -> webLookup(onDay, from, to));
    }

    private static @NotNull Optional<Map<String, BigDecimal>> cacheLookup(@NotNull LocalDate onDay, @NotNull String from, @NotNull List<String> to) {
        if (cache.containsKey(onDay) && cache.get(onDay).containsKey(from) && cache.get(onDay).get(from).keySet().containsAll(to))
            return Optional.of(cache.get(onDay).get(from));
        else
            return Optional.empty();
    }

    public static void clearCache() {
        cache.clear();
    }

    private static void updateCache(@NotNull LocalDate onDay, @NotNull String from, @NotNull String to, @NotNull BigDecimal exchangeRate) {
        if (!cache.containsKey(onDay))
            cache.put(onDay, new HashMap<>());

        if (!cache.get(onDay).containsKey(from))
            cache.get(onDay).put(from, new HashMap<>());

        cache.get(onDay).get(from).put(to, exchangeRate);
    }

    private static @NotNull Map<String, BigDecimal> webLookup(@NotNull LocalDate onDay, @NotNull String from, @NotNull List<String> to) {
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri(onDay, from, to))
                    .timeout(Duration.ofSeconds(5))
                    .GET()
                    .build();

            try(JsonReader reader = Json.createReader(new StringReader(client.send(request, HttpResponse.BodyHandlers.ofString()).body()))) {
                var exchangeRates = reader.readObject().getJsonObject("rates");

                for (String exchangeTo: exchangeRates.keySet()) {
                    var exchangeRate = exchangeRates.getJsonNumber(exchangeTo).bigDecimalValue();

                    updateCache(onDay, from, exchangeTo, exchangeRate);
                }

                return cache.get(onDay).get(from);
            }
        } catch (Exception _) {
            return new HashMap<>();
        }
    }

    private static @NotNull URI uri(@NotNull LocalDate date, @NotNull String baseCurrency, @NotNull List<String> exchangeCurrencies) {
        return URI.create("https://api.frankfurter.dev/v1/" + date + uriParams(baseCurrency, exchangeCurrencies));
    }

    private static @NotNull String uriParams(@NotNull String baseCurrency, @NotNull List<String> exchangeCurrencies) {
        StringBuilder builder = new StringBuilder("?amount=1");

        if (!baseCurrency.isBlank()) {
            builder.append("&base=");
            builder.append(baseCurrency);
        }

        if(!exchangeCurrencies.isEmpty()) {
            builder.append("&symbols=");
            for (String exchangeCurrency : exchangeCurrencies) {
                builder.append(exchangeCurrency);
                builder.append(",");
            }
        }

        return builder.toString();
    }
}
