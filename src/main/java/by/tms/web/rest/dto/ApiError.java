package by.tms.web.rest.dto;

import java.time.Instant;
import java.util.Map;

/**
 * Uniform JSON error body produced by {@code RestExceptionHandler},
 * {@code JsonAuthenticationEntryPoint} and {@code JsonAccessDeniedHandler}.
 *
 * @param status    HTTP status code
 * @param message   human readable message
 * @param timestamp moment the error occurred
 * @param errors    field name -> validation message (may be empty)
 */
public record ApiError(int status, String message, Instant timestamp, Map<String, String> errors) {

    public static ApiError of(int status, String message) {
        return new ApiError(status, message, Instant.now(), Map.of());
    }

    public static ApiError of(int status, String message, Map<String, String> errors) {
        return new ApiError(status, message, Instant.now(), errors == null ? Map.of() : errors);
    }
}
