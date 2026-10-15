package by.tms.web.rest.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

import java.util.List;

/**
 * Body for creating an order. The shopping cart now lives in the SPA,
 * so the client sends the selected book ids instead of a server side session cart.
 *
 * @param bookIds      ids of the books to rent
 * @param rentalPeriod rental period in days (is mapped to a due date on the server)
 */
public record CreateOrderRequest(@NotEmpty List<Long> bookIds,
                                 @Positive int rentalPeriod) {
}
