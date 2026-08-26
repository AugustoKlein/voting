package com.voting.client.service.service.impl;

import com.voting.client.service.dto.DocumentValidationResponse;
import com.voting.client.service.repository.DocumentValidationRepository;
import com.voting.client.service.service.DocumentValidationClientService;
import com.voting.infra.exception.MemberUnableToVoteException;
import feign.FeignException;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Profile({"dev", "prd"})
public class DocumentValidationClientServiceImpl implements DocumentValidationClientService {

    private DocumentValidationRepository documentValidationRepository;

    @Override
    public DocumentValidationResponse validateDocumentId(String name) {
        try {
            return documentValidationRepository.validate(name);
        } catch (FeignException.FeignClientException exception) {
            throw new MemberUnableToVoteException();
        }
    }
}
