package com.voting.client.service.repository;

import com.voting.client.service.dto.DocumentValidationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(url = "${cpf.url}")
public interface DocumentValidationRepository {

    @GetMapping("/api/cpf-fake-validation")
    DocumentValidationResponse validate(String cpf);
}
