package com.voting.client.service.service.impl;

import com.voting.client.service.dto.DocumentValidationResponse;
import com.voting.voter.enums.VoterStatusEnum;
import com.voting.client.service.service.DocumentValidationClientService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@Profile("local")
public class DocumentValidationClientServiceMock implements DocumentValidationClientService {

    @Override
    public DocumentValidationResponse validateDocumentId(String cpf) {
        int number = new Random().nextInt(100);
        VoterStatusEnum status = number >= 50 ? VoterStatusEnum.ABLE_TO_VOTE : VoterStatusEnum.UNABLE_TO_VOTE;
        return DocumentValidationResponse.builder()
                .status(status)
                .build();
    }
}
