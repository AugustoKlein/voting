package com.voting.infra.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class GenericErrorDto {
    private String status;
    private String message;
}
