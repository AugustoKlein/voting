package com.voting.client.service.service;

import com.voting.client.service.dto.DocumentValidationResponse;

public interface DocumentValidationClientService {

    DocumentValidationResponse validateDocumentId(String cpf);
}
