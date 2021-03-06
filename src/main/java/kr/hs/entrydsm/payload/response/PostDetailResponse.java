package kr.hs.entrydsm.payload.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDetailResponse {

    private long id;

    private String imagePath;

    private String description;

    private String title;

    private String author;

    private String content;

    private LocalDateTime date;

}

