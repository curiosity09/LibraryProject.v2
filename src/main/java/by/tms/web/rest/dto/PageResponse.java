package by.tms.web.rest.dto;

import java.util.List;

/**
 * Serializable page wrapper returned by every paged REST endpoint.
 *
 * @param content     items of the current page
 * @param page        zero-based page index
 * @param size        requested page size
 * @param totalPages  total amount of pages
 * @param hasNext     {@code true} when a next page exists
 * @param hasPrevious {@code true} when a previous page exists
 */
public record PageResponse<T>(List<T> content,
                              int page,
                              int size,
                              int totalPages,
                              boolean hasNext,
                              boolean hasPrevious) {

    public static <T> PageResponse<T> of(List<T> content, int page, int size, int totalPages) {
        return new PageResponse<>(content, page, size, totalPages, page + 1 < totalPages, page > 0);
    }
}
