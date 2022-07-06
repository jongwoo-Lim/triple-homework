package com.triple.triplehomework.handler;

import com.triple.triplehomework.controller.ReviewController;
import com.triple.triplehomework.dto.ResponseDto;
import com.triple.triplehomework.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = ReviewController.class)
@RequiredArgsConstructor
@Slf4j
public class ReviewExceptionHandler {

    private final ResponseDto responseDto;

    @ExceptionHandler(PlaceNotFoundException.class)
    public ResponseEntity<?> placeNotFoundException(PlaceNotFoundException ex){
        log.info("error message: {}", ex.getMessage());
        return responseDto.badRequest(ex.getMessage());
    }

    @ExceptionHandler(PhotoExistException.class)
    public ResponseEntity<?> photoExistException(PhotoExistException ex){
        log.info("error message: {}", ex.getMessage());
        return responseDto.badRequest(ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> httpMessageNotReadableException(HttpMessageNotReadableException ex){
        log.info("error message: {}", ex.getMessage());
        return responseDto.badRequest("잘못된 리뷰 이벤트 요청 형식");
    }

    @ExceptionHandler(ReviewExistException.class)
    public ResponseEntity<?> reviewExistException(ReviewExistException ex){
        log.info("error message: {}", ex.getMessage());
        return responseDto.badRequest(ex.getMessage());
    }

    @ExceptionHandler(ReviewNotFoundException.class)
    public ResponseEntity<?> reviewNotFoundException(ReviewNotFoundException ex){
        log.info("error message: {}", ex.getMessage());
        return responseDto.badRequest(ex.getMessage());
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<?> memberNotFoundException(MemberNotFoundException ex){
        log.info("error message: {}", ex.getMessage());
        return responseDto.badRequest(ex.getMessage());
    }
}
