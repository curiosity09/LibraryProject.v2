package by.tms.web.rest.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * The repository layer works with a fixed page size of ten
 * ({@code ServiceUtil.LIMIT_TEN}) and exposes the list of page offsets.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PagingUtils {

    public static final int PAGE_SIZE = 10;

    public static int normalizePage(int page) {
        return Math.max(page, 0);
    }

    public static int offset(int page) {
        return normalizePage(page) * PAGE_SIZE;
    }

    public static int totalPages(List<Long> pageOffsets) {
        return pageOffsets == null ? 0 : pageOffsets.size();
    }
}
