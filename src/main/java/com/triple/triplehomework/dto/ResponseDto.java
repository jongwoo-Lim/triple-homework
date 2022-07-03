package com.triple.triplehomework.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.triple.triplehomework.common.ErrorSerializer;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.Collections;

@Component
public class ResponseDto {

    @Getter
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class Body{
        private int status;
        private String message;
        private Object data;
        @JsonSerialize(using = ErrorSerializer.class)
        private Errors errors;
    }

    public ResponseEntity<?> ok(Object data, String msg, HttpStatus status) {
        Body body = Body.builder()
                .status(status.value())
                .data(data)
                .message(msg)
                .build();
        return ResponseEntity.ok(body);
    }

    public ResponseEntity<?> ok(String msg) {
        return ok(Collections.emptyList(), msg, HttpStatus.OK);
    }

    public ResponseEntity<?> badRequest(Object data, String msg, HttpStatus status) {
        Body body = Body.builder()
                .status(status.value())
                .data(data)
                .message(msg)
                .build();
        return ResponseEntity.badRequest().body(body);
    }

    public ResponseEntity<?> badRequest(Object data, String msg, Errors errors, HttpStatus status) {
        Body body = Body.builder()
                .status(status.value())
                .data(data)
                .message(msg)
                .errors(errors)
                .build();
        return ResponseEntity.badRequest().body(body);
    }

    public ResponseEntity<?> badRequest(String msg) {
        return badRequest(Collections.emptyList(), msg, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> badRequest(Errors errors, String msg) {
        return badRequest(Collections.emptyList(), msg, errors, HttpStatus.BAD_REQUEST);
    }
}
