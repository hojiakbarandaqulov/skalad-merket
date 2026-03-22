package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PageMeta {
    private long total;
    private int page;
    private int perPage;
    private int totalPages;
}
