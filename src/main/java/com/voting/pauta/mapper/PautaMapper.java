package com.voting.pauta.mapper;

import com.voting.pauta.controller.request.PautaRequest;
import com.voting.pauta.controller.response.PautaResponse;
import com.voting.pauta.dto.PautaDto;
import com.voting.pauta.enums.PautaStatusEnum;
import com.voting.pauta.repository.entity.Pauta;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PautaMapper {

    public static PautaDto toDto(PautaRequest pautaRequest) {
        return PautaDto.builder()
                .name(pautaRequest.getName())
                .description(pautaRequest.getDescription())
                .build();
    }

    public static Pauta toEntity(PautaDto pautaDto) {
        return Pauta.builder()
                .name(pautaDto.name())
                .description(pautaDto.description())
                .status(pautaDto.status())
                .endsAt(pautaDto.endsAt())
                .status(PautaStatusEnum.CREATED)
                .totalVoteCount(pautaDto.totalVoteCount())
                .yesVoteCount(pautaDto.yesVoteCount())
                .noVoteCount(pautaDto.noVoteCount())
                .build();
    }

    public static PautaResponse toResponse(PautaDto pautaDto) {
        return PautaResponse.builder()
                .id(pautaDto.id())
                .name(pautaDto.name())
                .description(pautaDto.description())
                .status(pautaDto.status())
                .endsAt(pautaDto.endsAt())
                .status(pautaDto.status())
                .totalVoteCount(pautaDto.totalVoteCount())
                .yesVoteCount(pautaDto.yesVoteCount())
                .noVoteCount(pautaDto.noVoteCount())
                .build();
    }

    public static PautaDto toDto(Pauta pauta) {
        return PautaDto.builder()
                .id(pauta.getId())
                .name(pauta.getName())
                .description(pauta.getDescription())
                .status(pauta.getStatus())
                .endsAt(pauta.getEndsAt())
                .status(pauta.getStatus())
                .totalVoteCount(pauta.getTotalVoteCount())
                .yesVoteCount(pauta.getYesVoteCount())
                .noVoteCount(pauta.getNoVoteCount())
                .build();
    }
}
