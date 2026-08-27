package com.voting.pauta.controller.request;

import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PautaVoteRequest {

    @NonNull
    private String cpf;

    @NonNull
    private Boolean votedYes;
}
