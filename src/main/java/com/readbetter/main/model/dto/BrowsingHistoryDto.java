package com.readbetter.main.model.dto;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrowsingHistoryDto {
    private String url;
    private LocalDateTime timestamp;
}
