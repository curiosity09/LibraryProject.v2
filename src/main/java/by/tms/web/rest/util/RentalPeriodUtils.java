package by.tms.web.rest.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/**
 * Business rule that converts a rental period (days) into the moment the books must be returned.
 * One day means "until 19:00 today, or until 19:00 tomorrow when it is already past 19:00".
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RentalPeriodUtils {

    private static final LocalTime DUE_TIME = LocalTime.of(19, 0);

    public static LocalDateTime dueDate(int rentalPeriod) {
        LocalDateTime now = LocalDateTime.now();
        if (rentalPeriod == 1) {
            long seconds = now.compareTo(LocalDateTime.of(LocalDate.now(), DUE_TIME)) > 0
                    ? ChronoUnit.SECONDS.between(LocalDateTime.of(LocalDate.now().plusDays(1), DUE_TIME), now)
                    : ChronoUnit.SECONDS.between(LocalTime.now(), DUE_TIME);
            return now.plusSeconds(Math.abs(seconds));
        }
        return now.plusDays(rentalPeriod);
    }
}
