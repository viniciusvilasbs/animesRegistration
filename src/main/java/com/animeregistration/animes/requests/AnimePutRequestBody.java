package com.animeregistration.animes.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnimePutRequestBody {

    @Schema(description = "This is the anime's id", example = "1")
    private Long id;

    @NotEmpty(message = "The name cannot be empty nor null!")
    @Schema(description = "This is the anime's name", example = "Dragon Ball Super")
    private String name;
}
