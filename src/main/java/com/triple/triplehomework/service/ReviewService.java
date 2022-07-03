package com.triple.triplehomework.service;

import com.triple.triplehomework.dto.ReviewRequestDto;
import com.triple.triplehomework.dto.ReviewResponseDto;
import com.triple.triplehomework.entity.review.Review;

import java.util.List;

public interface ReviewService {

    // 리뷰 작성
    ReviewResponseDto register(ReviewRequestDto reviewRequestDto);
    // 리뷰 수정
    ReviewResponseDto modify(ReviewRequestDto reviewRequestDto);
    // 리뷰 삭제
    boolean remove(ReviewRequestDto reviewRequestDto);

    default ReviewResponseDto entityToDto(Review review, List<String> photos){

        return ReviewResponseDto.builder()
                    .reviewId(review.getReviewId().toString())
                    .content(review.getContent())
                    .userId(review.getUserId().toString())
                    .placeId(review.getPlace().getPlaceId().toString())
                    .attachedPhotoIds(photos)
                    .removeYn(review.getRemoveYn())
                    .regDate(review.getRegDate())
                    .updDate(review.getUpdDate())
                    .build();
    }
}
