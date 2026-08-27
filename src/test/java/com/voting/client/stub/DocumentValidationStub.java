package com.voting.client.stub;

import com.voting.client.service.dto.DocumentValidationResponse;
import com.voting.voter.enums.VoterStatusEnum;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DocumentValidationStub {
    public static DocumentValidationResponse documentValidationResponse(VoterStatusEnum status) {
        return DocumentValidationResponse.builder()
                .status(status)
                .build();
    }
}
