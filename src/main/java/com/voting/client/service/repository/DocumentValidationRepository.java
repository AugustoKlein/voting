package com.voting.client.service.repository;

import com.voting.client.service.dto.DocumentValidationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

//this is a fake/mock api just to show OpenFeign functionality
@FeignClient(url = "${cpf.url}")
public interface DocumentValidationRepository {

    @GetMapping("/api/cpf-validation")
    DocumentValidationResponse validate(String cpf);
}
