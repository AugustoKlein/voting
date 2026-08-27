package com.voting.infra.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CreatedPautaException extends RuntimeException {
    public CreatedPautaException(String message) {
        super(message);
    }
}
