package com.voting.client.service;

import com.voting.client.service.dto.DocumentValidationResponse;
import com.voting.client.service.repository.DocumentValidationRepository;
import com.voting.client.service.service.impl.DocumentValidationClientServiceImpl;
import com.voting.client.stub.DocumentValidationStub;
import com.voting.infra.exception.MemberUnableToVoteException;
import com.voting.voter.enums.VoterStatusEnum;
import feign.FeignException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DisplayName("Document Validation Service Mock Test")
@ExtendWith(MockitoExtension.class)
class DocumentValidationClientServiceTest {
    public static final String CPF = "1234567890";

    @InjectMocks
    private DocumentValidationClientServiceImpl documentValidationClientService;

    @Mock
    private DocumentValidationRepository documentValidationRepository;

    @Nested
    @DisplayName("ValidateDocumentId")
    class ValidateDocumentId {

        @Test
        @DisplayName("Should return the voter status")
        public void validateDocumentId() {
            when(documentValidationRepository.validate(anyString()))
                    .thenReturn(DocumentValidationStub.documentValidationResponse(VoterStatusEnum.ABLE_TO_VOTE));
            DocumentValidationResponse response = documentValidationClientService.validateDocumentId(CPF);

            assertNotNull(response.status());
            verify(documentValidationRepository).validate(anyString());
        }

        @Test
        @DisplayName("Should throw MemberUnableToVoteException when the api responses with an error")
        public void validateDocumentIdMemberUnableToVoteException() {
            when(documentValidationRepository.validate(anyString()))
                    .thenThrow(FeignException.FeignClientException.class);

            assertThrows(MemberUnableToVoteException.class, () -> documentValidationClientService.validateDocumentId(CPF));

            verify(documentValidationRepository).validate(anyString());
        }
    }
}
