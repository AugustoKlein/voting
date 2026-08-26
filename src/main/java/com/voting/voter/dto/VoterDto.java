package com.voting.voter.dto;

import lombok.Builder;

@Builder
public record VoterDto(Long id,
                       String cpf,
                       boolean votedYes,
                       Long pautaId
    ) {
}
