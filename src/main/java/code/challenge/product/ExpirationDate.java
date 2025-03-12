package code.challenge.product;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;

public sealed interface ExpirationDate permits ExpirationDate.ExpiresAt, ExpirationDate.DoesNotExpire {

    record ExpiresAt(@NotNull LocalDate expirationDate) implements ExpirationDate {
        @Override
        public String toString() {
            return expirationDate.toString();
        }
    }

    final class DoesNotExpire implements ExpirationDate {
        private DoesNotExpire() {}

        private static @Nullable DoesNotExpire INSTANCE;

        public static @NotNull DoesNotExpire instance() {
            if (INSTANCE == null)
                INSTANCE = new DoesNotExpire();

            return INSTANCE;
        }

        @Override
        public String toString() {
            return "____-__-__";
        }
    }
}