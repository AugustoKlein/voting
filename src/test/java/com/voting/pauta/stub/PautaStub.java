package com.voting.pauta.stub;

import com.voting.pauta.controller.request.OpenPautaRequest;
import com.voting.pauta.controller.request.PautaRequest;
import com.voting.pauta.controller.request.PautaVoteRequest;
import com.voting.pauta.controller.response.PautaResponse;
import com.voting.pauta.dto.PautaDto;
import com.voting.pauta.enums.PautaStatusEnum;
import com.voting.pauta.repository.entity.Pauta;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PautaStub {

    public static Pauta pauta() {
        return Pauta.builder()
                .id(1L)
                .name("Assembly Voting 1")
                .description("Should we continue with daily meetings")
                .status(PautaStatusEnum.CREATED)
                .endsAt(LocalDateTime.now())
                .totalVoteCount(4L)
                .yesVoteCount(2L)
                .noVoteCount(2L)
                .build();
    }

    public static Pauta pauta(PautaStatusEnum status) {
        Pauta pauta = pauta();
        pauta.setStatus(status);
        return pauta;
    }

    public static PautaDto pautaDto() {
        return PautaDto.builder()
                .id(1L)
                .name("Assembly Voting 1")
                .description("Should we continue with daily meetings")
                .status(PautaStatusEnum.OPEN)
                .endsAt(LocalDateTime.now())
                .totalVoteCount(4L)
                .yesVoteCount(2L)
                .noVoteCount(2L)
                .build();
    }

    public static PautaResponse pautaResponse() {
        return PautaResponse.builder()
                .id(1L)
                .name("Assembly Voting 1")
                .description("Should we continue with daily meetings")
                .status(PautaStatusEnum.OPEN)
                .endsAt(LocalDateTime.now())
                .totalVoteCount(4L)
                .yesVoteCount(2L)
                .noVoteCount(2L)
                .build();
    }

    public static PautaRequest pautaRequest() {
        return PautaRequest.builder()
                .name("Assembly Voting 1")
                .description("Should we continue with daily meetings")
                .build();
    }

    public static PautaVoteRequest pautaVoteRequest() {
        return PautaVoteRequest.builder()
                .cpf("54261727064")
                .votedYes(true)
                .build();
    }

    public static OpenPautaRequest openPautaRequest() {
        return OpenPautaRequest.builder()
                .endsAt(LocalDateTime.now().plusMinutes(10))
                .build();
    }


}
