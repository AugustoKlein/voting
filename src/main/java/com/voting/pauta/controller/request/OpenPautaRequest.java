package com.voting.pauta.controller.request;

import jakarta.validation.constraints.FutureOrPresent;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OpenPautaRequest {

    @FutureOrPresent(message = "Cannot be open in the past")
    private LocalDateTime endsAt;
}
