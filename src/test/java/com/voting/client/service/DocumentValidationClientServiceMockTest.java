package com.voting.client.service;

import com.voting.client.service.dto.DocumentValidationResponse;
import com.voting.client.service.service.impl.DocumentValidationClientServiceMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Document Validation Service Mock Test")
@ExtendWith(MockitoExtension.class)
class DocumentValidationClientServiceMockTest {
    public static final String CPF = "1234567890";

    @InjectMocks
    private DocumentValidationClientServiceMock documentValidationClientServiceMock;

    @Nested
    @DisplayName("ValidateDocumentId")
    class ValidateDocumentId {

        @Test
        @DisplayName("Should return a random voter status")
        public void validateDocumentId() {
            DocumentValidationResponse response = documentValidationClientServiceMock.validateDocumentId(CPF);

            assertNotNull(response.status());
        }
    }
}
