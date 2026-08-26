package com.voting.infra.handler;

import com.voting.infra.exception.ExpiredPautaException;
import com.voting.infra.dto.ErrorDto;
import com.voting.infra.exception.MemberUnableToVoteException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MyExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ErrorDto handleEntityNotFoundException(EntityNotFoundException exception) {
        return ErrorDto.builder()
                .status(HttpStatus.NOT_FOUND.name())
                .build();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorDto handleIllegalArgumentException(IllegalArgumentException exception) {
        return ErrorDto.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MemberUnableToVoteException.class)
    public ErrorDto handleMemberUnableToVoteException(MemberUnableToVoteException exception) {
        return ErrorDto.builder()
                .status(exception.getMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ExpiredPautaException.class)
    public ErrorDto handleExpiredPautaException(ExpiredPautaException exception) {
        return ErrorDto.builder()
                .status("EXPIRED_PAUTA")
                .build();
    }

}