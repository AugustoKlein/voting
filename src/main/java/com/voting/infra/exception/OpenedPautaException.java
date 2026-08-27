package com.voting.infra.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OpenedPautaException extends RuntimeException {
    public OpenedPautaException(String message) {
        super(message);
    }
}
