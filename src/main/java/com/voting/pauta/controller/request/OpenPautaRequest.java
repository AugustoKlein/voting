package com.voting.pauta.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Dados necessários para abertura da sessão de votação")
public class OpenPautaRequest {

    @Schema(
            description = "Data e hora em que a sessão de votação será encerrada",
            example = "2026-08-27T18:30:00",
            type = "string",
            format = "date-time"
    )
    @FutureOrPresent(message = "Cannot be open in the past")
    private LocalDateTime endsAt;
}
