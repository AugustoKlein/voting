package com.voting.client.service.dto;

import com.voting.voter.enums.VoterStatusEnum;
import lombok.Builder;

@Builder
public record DocumentValidationResponse(VoterStatusEnum status) {
}
