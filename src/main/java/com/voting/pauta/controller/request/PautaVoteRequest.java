package com.voting.pauta.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Dados necessários para registrar um voto")
public class PautaVoteRequest {

    @Schema(
            description = "CPF do eleitor",
            example = "54261727064"
    )
    @NotNull
    @CPF
    private String cpf;

    @Schema(
            description = "Indica se o voto é favorável à pauta",
            example = "true"
    )
    @NotNull
    private Boolean votedYes;
}
