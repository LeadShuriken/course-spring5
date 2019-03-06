package com.example.mvcDemo.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Document(collection = "articles")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Article {
    @Id
    private String id;

    private String pictureUrl;

    @NotNull @Size(min = 3, max = 60) // Bean Validation
    @NonNull
    private String title;

    @NotBlank
    @NonNull
    private String content;

    @PastOrPresent
    private LocalDateTime created = LocalDateTime.now();

    @PastOrPresent
    private LocalDateTime modified = LocalDateTime.now();
}
