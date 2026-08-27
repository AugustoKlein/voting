package com.voting.infra.handler;

import com.voting.infra.dto.GenericErrorDto;
import com.voting.infra.exception.ClosedPautaException;
import com.voting.infra.dto.ErrorDto;
import com.voting.infra.exception.CreatedPautaException;
import com.voting.infra.exception.MemberUnableToVoteException;
import com.voting.infra.exception.OpenedPautaException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
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
    @ExceptionHandler(ClosedPautaException.class)
    public ErrorDto handleClosedPautaException(ClosedPautaException exception) {
        return ErrorDto.builder()
                .status("PAUTA_HAS_BEEN_CLOSED")
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(OpenedPautaException.class)
    public ErrorDto handlePautaIsAlreadyOpenedException(OpenedPautaException exception) {
        return ErrorDto.builder()
                .status("PAUTA_IS_ALREADY_OPENED")
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CreatedPautaException.class)
    public ErrorDto handleCreatedPautaException(CreatedPautaException exception) {
        return ErrorDto.builder()
                .status("PAUTA_IS_CREATED_BUT_NOT_OPENED")
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public GenericErrorDto handleException(Exception ex) {
        return GenericErrorDto.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .message(ex.getMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public GenericErrorDto handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return GenericErrorDto.builder()
                .status(HttpStatus.BAD_REQUEST.name())
                .message(Arrays.asList(ex.getDetailMessageArguments()).getLast().toString())
                .build();
    }
}