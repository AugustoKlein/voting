package com.voting.infra.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ExpiredPautaException extends RuntimeException {
    public ExpiredPautaException(String message) {
        super(message);
    }
}
