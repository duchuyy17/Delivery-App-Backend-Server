package com.laptrinhjavaweb.news.mongo;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "options")
public class OptionDocument {
    @Id
    private String id;
    private String title;
    private String description;
    private Double price;

    public String get_id(){
        return id;
    }
}
