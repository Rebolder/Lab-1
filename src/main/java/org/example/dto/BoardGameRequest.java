package org.example.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardGameRequest {

    @NotNull
    private Long publisherId;

    @NotBlank
    private String slug;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    @Min(1)
    @Max(20)
    private Integer minPlayers;

    @NotNull
    @Min(1)
    @Max(20)
    private Integer maxPlayers;

    @NotNull
    @Min(5)
    private Integer playTimeMinutes;
}
