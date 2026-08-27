package com.voting.pauta.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Dados necessários para registrar um voto")
public class PautaVoteRequest {

    @Schema(
            description = "CPF do eleitor",
            example = "12345678900"
    )
    @NonNull
    private String cpf;

    @Schema(
            description = "Indica se o voto é favorável à pauta",
            example = "true"
    )
    @NonNull
    private Boolean votedYes;
}
