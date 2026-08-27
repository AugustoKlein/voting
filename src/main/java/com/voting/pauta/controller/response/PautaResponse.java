package com.voting.pauta.controller.response;

import com.voting.pauta.enums.PautaStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PautaResponse {

    @Schema(
            description = "Identificador da pauta",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Nome da pauta",
            example = "Implantação de home office"
    )
    private String name;

    @Schema(
            description = "Descrição da pauta",
            example = "Permitir trabalho remoto três vezes por semana"
    )
    private String description;

    @Schema(
            description = "Status atual da pauta",
            example = "OPEN"
    )
    private PautaStatusEnum status;

    @Schema(
            description = "Data e hora de encerramento da votação",
            example = "2026-08-27T18:30:00"
    )
    private LocalDateTime endsAt;

    @Schema(
            description = "Quantidade total de votos",
            example = "10"
    )
    private Long totalVoteCount;

    @Schema(
            description = "Quantidade de votos SIM",
            example = "7"
    )
    private Long yesVoteCount;

    @Schema(
            description = "Quantidade de votos NÃO",
            example = "3"
    )
    private Long noVoteCount;

}
