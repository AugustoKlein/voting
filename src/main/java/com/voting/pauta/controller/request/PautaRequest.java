package com.voting.pauta.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Dados necessários para criação de uma pauta")
public class PautaRequest {

    @Schema(
            description = "Nome da pauta",
            example = "Implantação de home office"
    )
    @NotNull
    private String name;

    @Schema(
            description = "Descrição da pauta",
            example = "Permitir trabalho remoto três vezes por semana"
    )
    @NotNull
    private String description;
}
