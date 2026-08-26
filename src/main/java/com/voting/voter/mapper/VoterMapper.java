package com.voting.voter.mapper;

import com.voting.voter.dto.VoterDto;
import com.voting.voter.repository.entity.Voter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class VoterMapper {

    public static Voter toVoter(VoterDto voterDto) {
        return Voter.builder()
                .cpf(voterDto.cpf())
                .pautaId(voterDto.pautaId())
                .votedYes(voterDto.votedYes())
                .build();
    }

    public static VoterDto toVoterDto(Voter voter) {
        return VoterDto.builder()
                .id(voter.getId())
                .cpf(voter.getCpf())
                .votedYes(voter.getVotedYes())
                .build();
    }
}
