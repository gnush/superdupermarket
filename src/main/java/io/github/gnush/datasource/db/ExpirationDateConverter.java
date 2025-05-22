package io.github.gnush.datasource.db;

import io.github.gnush.commodity.ExpirationDate;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.LocalDate;

@Converter
public class ExpirationDateConverter implements AttributeConverter<ExpirationDate, LocalDate> {
    @Override
    public LocalDate convertToDatabaseColumn(ExpirationDate expirationDate) {
        return switch (expirationDate) {
            case null -> null;
            case ExpirationDate.DoesNotExpire _ -> null;
            case ExpirationDate.ExpiresAt expires -> expires.expirationDate();
        };
    }

    @Override
    public ExpirationDate convertToEntityAttribute(LocalDate localDate) {
        if (localDate == null)
            return ExpirationDate.DoesNotExpire.instance();
        else
            return new ExpirationDate.ExpiresAt(localDate);
    }
}
