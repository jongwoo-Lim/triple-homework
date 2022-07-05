package com.triple.triplehomework.handler;

import com.triple.triplehomework.controller.MemberController;
import com.triple.triplehomework.controller.ReviewController;
import com.triple.triplehomework.dto.ResponseDto;
import com.triple.triplehomework.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = MemberController.class)
@RequiredArgsConstructor
@Slf4j
public class MemberExceptionHandler {

    private final ResponseDto responseDto;

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<?> memberNotFoundException(MemberNotFoundException ex){
        log.info("error message: {}", ex.getMessage());
        return responseDto.badRequest(ex.getMessage());
    }
}
