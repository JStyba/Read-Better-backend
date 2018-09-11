package com.readbetter.main.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data

public class PageResponse<T> extends Response {
    private int currentPage;
    private int totalElements;

    public PageResponse(Page<T> objects) {
        this.currentPage = objects.getNumber();
        this.totalElements = objects.getNumberOfElements();

        List<T> objects1 = objects.getContent();
    }
}