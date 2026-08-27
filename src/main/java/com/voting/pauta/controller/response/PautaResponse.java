package com.voting.pauta.controller.response;

import com.voting.pauta.enums.PautaStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PautaResponse {

    private Long id;
    private String name;
    private String description;
    private PautaStatusEnum status;
    private LocalDateTime endsAt;
    private Long totalVoteCount;
    private Long yesVoteCount;
    private Long noVoteCount;

}
