package com.voting.pauta.mapper;

import com.voting.pauta.controller.request.PautaRequest;
import com.voting.pauta.dto.PautaDto;
import com.voting.pauta.enums.PautaStatusEnum;
import com.voting.pauta.repository.entity.Pauta;
import com.voting.voter.dto.PautaVoterDto;
import com.voting.voter.repository.entity.PautaVoterId;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PautaMapper {

    public static PautaDto toDto(PautaRequest pautaRequest) {
        return PautaDto.builder()
                .name(pautaRequest.getName())
                .description(pautaRequest.getDescription())
                .expirationTime(pautaRequest.getExpirationTime())
                .build();
    }

    public static Pauta toEntity(PautaDto pautaDto) {
        return Pauta.builder()
                .name(pautaDto.name())
                .description(pautaDto.description())
                .statusEnum(pautaDto.status())
                .expirationTime(pautaDto.expirationTime())
                .statusEnum(PautaStatusEnum.IN_PROGRESS)
                .build();
    }

    public static PautaVoterId toId(PautaVoterDto pautaVoterDto) {
        return PautaVoterId.builder()
                .pautaId(pautaVoterDto.pautaId())
                .memberId(pautaVoterDto.memberId())
                .build();
    }
}
