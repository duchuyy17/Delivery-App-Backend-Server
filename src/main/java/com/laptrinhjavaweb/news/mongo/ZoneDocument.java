package com.laptrinhjavaweb.news.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ZoneDocument {
    @Id
    private String id;
    private String title;

    public String get_id() {
        return id;
    }
}
