package com.voting.infra.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ClosedPautaException extends RuntimeException {
    public ClosedPautaException(String message) {
        super(message);
    }
}
