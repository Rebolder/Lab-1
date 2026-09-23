package org.example.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardGameResponse {
    private Long id;
    private Long publisherId;
    private String publisherName;
    private String slug;
    private String name;
    private String description;
    private Integer minPlayers;
    private Integer maxPlayers;
    private Integer playTimeMinutes;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
