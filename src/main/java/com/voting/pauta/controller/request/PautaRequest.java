package com.voting.pauta.controller.request;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;

@Builder
@Data
public class PautaRequest {
    @NonNull
    private String name;

    @NonNull
    private String description;

    //2026-08-25T17:30:00
    @NonNull
    private LocalDateTime expirationTime;
}
