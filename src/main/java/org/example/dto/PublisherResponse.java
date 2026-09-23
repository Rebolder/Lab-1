package org.example.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublisherResponse {
    private Long id;
    private String name;
    private String email;
    private String country;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
