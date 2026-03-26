package org.example.service.impl;

import org.example.dto.PageMeta;
import org.example.dto.PagedResponse;

import java.util.List;

public final class ServiceHelper {

    private ServiceHelper() {
    }

    public static <T> PagedResponse<T> toPagedResponse(List<T> items, int page, int perPage) {
        int safePage = Math.max(page, 1);
        int safePerPage = Math.max(perPage, 1);
        int fromIndex = Math.min((safePage - 1) * safePerPage, items.size());
        int toIndex = Math.min(fromIndex + safePerPage, items.size());
        int totalPages = items.isEmpty() ? 0 : (int) Math.ceil((double) items.size() / safePerPage);

        return new PagedResponse<>(
                items.subList(fromIndex, toIndex),
                new PageMeta(items.size(), safePage, safePerPage, totalPages)
        );
    }
}
