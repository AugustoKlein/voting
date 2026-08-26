package com.voting.pauta.dto;

import com.voting.voter.dto.VoterDto;
import com.voting.pauta.enums.PautaStatusEnum;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record PautaDto(Long id,
                       String name,
                       String description,
                       List<VoterDto> memberList,
                       PautaStatusEnum status,
                       LocalDateTime expirationTime) {
}
