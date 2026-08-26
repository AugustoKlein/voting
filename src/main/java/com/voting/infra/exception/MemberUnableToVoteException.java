package com.voting.infra.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MemberUnableToVoteException extends RuntimeException {
    public MemberUnableToVoteException(String message) {
        super(message);
    }
}
