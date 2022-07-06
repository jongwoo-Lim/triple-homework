package com.triple.triplehomework.controller;

import com.triple.triplehomework.common.code.ReviewActionCode;
import com.triple.triplehomework.dto.ResponseDto;
import com.triple.triplehomework.dto.ReviewRequestDto;
import com.triple.triplehomework.dto.ReviewResponseDto;
import com.triple.triplehomework.service.ReviewService;
import com.triple.triplehomework.swagger.SwaggerErrorResponseDto;
import com.triple.triplehomework.swagger.SwaggerReviewResponseDto;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

    @ApiResponses({
            @ApiResponse(code = 200, message = "여행자 리뷰 작성,수정,삭제 성공", response = SwaggerReviewResponseDto.class),
            @ApiResponse(code = 400, message = "여행자 리뷰 요청 실패", response = SwaggerErrorResponseDto.class)
    })
    @PostMapping("/events")
    public ResponseEntity<?> reviewEvent(
            @RequestBody @Valid ReviewRequestDto reviewRequestDto, Errors errors){

        if(errors.hasErrors()){
            return responseDto.badRequest(errors, "요청 정보 에러");
        }

        if(isReviewChecked(reviewRequestDto.getType())){
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

        return responseDto.badRequest("잘못된 타입 이벤트 요청 ");
    }

    /**
     * 리뷰 요청 타입 체크
     * @param type
     * @return
     */
    private boolean isReviewChecked(String type) {
        return StringUtils.hasText(type) && type.equals("REVIEW");
    }
}
