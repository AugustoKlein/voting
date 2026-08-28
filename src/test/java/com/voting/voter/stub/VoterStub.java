package com.voting.voter.stub;

import com.voting.voter.dto.VoterDto;
import com.voting.voter.repository.entity.Voter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class VoterStub {
    public static Voter voter() {
        return Voter.builder()
                .id(1L)
                .cpf("54261727064")
                .votedYes(true)
                .pautaId(1L)
                .build();
    }

    public static VoterDto voterDto() {
        return VoterDto.builder()
                .id(1L)
                .cpf("54261727064")
                .votedYes(true)
                .pautaId(1L)
                .build();
    }
}
