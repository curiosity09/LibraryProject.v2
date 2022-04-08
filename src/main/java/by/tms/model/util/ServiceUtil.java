package by.tms.model.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ServiceUtil {

    private static final long LIMIT_TEN = 10;

    public static List<Long> collectPages(Long countRow) {
        long countPage;
        if (countRow % LIMIT_TEN != 0) {
            countPage = countRow / LIMIT_TEN + 1;
        } else {
            countPage = countRow / LIMIT_TEN;
        }
        List<Long> offsetList = new ArrayList<>();
        long num = 0;
        for (int i = 0; i < countPage; i++) {
            offsetList.add(num);
            num += LIMIT_TEN;
        }
        return offsetList;
    }
}
