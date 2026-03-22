package org.example.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import org.example.dto.PageMeta;
import org.example.dto.PagedResponse;
import org.springframework.data.domain.Page;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@UtilityClass
public class ServiceHelper {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static <T> PagedResponse<T> toPagedResponse(Page<T> page) {
        return new PagedResponse<>(page.getContent(), new PageMeta(page.getTotalElements(), page.getNumber() + 1, page.getSize(), page.getTotalPages()));
    }

    public static <T> PagedResponse<T> toPagedResponse(List<T> items, int page, int perPage) {
        int fromIndex = Math.min(Math.max(page - 1, 0) * perPage, items.size());
        int toIndex = Math.min(fromIndex + perPage, items.size());
        List<T> slice = items.subList(fromIndex, toIndex);
        int totalPages = perPage == 0 ? 0 : (int) Math.ceil((double) items.size() / perPage);
        return new PagedResponse<>(slice, new PageMeta(items.size(), page, perPage, totalPages));
    }

    public static Map<String, Object> readAttributes(String source) {
        if (source == null || source.isBlank()) {
            return Collections.emptyMap();
        }
        try {
            return OBJECT_MAPPER.readValue(source, new TypeReference<>() {
            });
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }

    public static String writeAttributes(Map<String, String> attributes) {
        if (attributes == null || attributes.isEmpty()) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(attributes);
        } catch (Exception e) {
            return "{}";
        }
    }
}
