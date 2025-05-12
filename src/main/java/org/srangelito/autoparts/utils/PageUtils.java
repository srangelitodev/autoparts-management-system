package org.srangelito.autoparts.utils;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public final class PageUtils {

    private PageUtils() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public static <T> Page<T> convertListToPage(List<T> fullList, int page, int pageSize) {
        int start = Math.min(page * pageSize, fullList.size());
        int end = Math.min(start + pageSize, fullList.size());
        List<T> sublist = fullList.subList(start, end);

        Pageable pageable = PageRequest.of(page, pageSize);
        return new PageImpl<>(sublist, pageable, fullList.size());
    }

}
