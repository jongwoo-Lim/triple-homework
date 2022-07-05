package com.triple.triplehomework.controller;

import com.triple.triplehomework.common.code.ReviewActionCode;
import com.triple.triplehomework.dto.ResponseDto;
import com.triple.triplehomework.dto.ReviewRequestDto;
import com.triple.triplehomework.dto.ReviewResponseDto;
import com.triple.triplehomework.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final ResponseDto responseDto;

    @PostMapping("/events")
    public ResponseEntity<?> reviewEvent(
            @RequestBody @Valid ReviewRequestDto reviewRequestDto, Errors errors){

        if(errors.hasErrors()){
            return responseDto.badRequest(errors, "요청 정보 에러");
        }

        final ReviewActionCode action = reviewRequestDto.getAction();
        final boolean hasReviewId = StringUtils.hasText(reviewRequestDto.getReviewId());

        switch (action){
            case ADD:

                if(StringUtils.hasText(reviewRequestDto.getContent())){
                    final ReviewResponseDto registerBody = reviewService.register(reviewRequestDto);
                    return responseDto.ok(registerBody, "리뷰 작성 성공", HttpStatus.OK);
                }
                return responseDto.badRequest("리뷰 내용은 비어있을 수 없습니다.");

            case MOD:

                if(hasReviewId){
                    final ReviewResponseDto modifyBody = reviewService.modify(reviewRequestDto);
                    return responseDto.ok(modifyBody, "리뷰 수정 성공", HttpStatus.OK);
                }
                return responseDto.badRequest("리뷰 ID는 필수값입니다.");

            case DELETE:

                if(hasReviewId){
                    final boolean removeBody = reviewService.remove(reviewRequestDto);
                    if(reviewRequestDto.getRemovePhotoYn().equalsIgnoreCase("Y")){
                        return responseDto.ok(removeBody, "첨부 파일 삭제 성공", HttpStatus.OK);
                    }
                    return responseDto.ok(removeBody, "리뷰 삭제 성공", HttpStatus.OK);
                }

            default:
                return responseDto.badRequest("리뷰 요청 이벤트 에러");
        }
    }
}
