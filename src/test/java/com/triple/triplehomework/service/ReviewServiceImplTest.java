package com.triple.triplehomework.service;

import com.triple.triplehomework.common.code.ReviewActionCode;
import com.triple.triplehomework.dto.ReviewRequestDto;
import com.triple.triplehomework.dto.ReviewResponseDto;
import com.triple.triplehomework.entity.review.AttachedPhoto;
import com.triple.triplehomework.entity.review.Review;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class ReviewServiceImplTest extends BaseServiceTest {

    @Test
    @DisplayName("리뷰 첨부파일 추가 테스트")
    public void modifyAttachedPhoto_test() throws Exception{
        //Given
        String content = "review test....";
        ReviewResponseDto reviewResponseDto = reviewService.register(reviewRequestDto);

        ReviewRequestDto updateRequest = ReviewRequestDto.builder()
                .type("REVIEW")
                .action(ReviewActionCode.MOD)
                .content(content)
                .reviewId(reviewResponseDto.getReviewId())
                .attachedPhotoIds(List.of("afb0cef2-851d-4a50-bb07-9cc15cbdc335"))
                .build();

        //When
        final ReviewResponseDto modifiedReview = reviewService.modify(updateRequest);

        //Then
        assertThat(modifiedReview.getAttachedPhotoIds().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("리뷰 내용 수정 테스트")
    public void modifyReview_test() throws Exception{
        //Given
        String updateContent = "review update content..";
        ReviewResponseDto reviewResponseDto = reviewService.register(reviewRequestDto);

        ReviewRequestDto updateRequest = ReviewRequestDto.builder()
                .type("REVIEW")
                .action(ReviewActionCode.MOD)
                .content(updateContent)
                .reviewId(reviewResponseDto.getReviewId())
                .attachedPhotoIds(Collections.EMPTY_LIST)
                .build();

        //When
        final ReviewResponseDto modifiedReview = reviewService.modify(updateRequest);

        //Then
        assertThat(modifiedReview.getContent()).isEqualTo(updateContent);
    }

    @Test
    @DisplayName("리뷰 등록 테스트")
    public void registerReview_test() throws Exception{
        // Given
        String content = "review test....";
        // When
        reviewService.register(reviewRequestDto);

        List<Review> reviews = reviewRepository.findAll();
        List<AttachedPhoto> photos = attachedPhotoRepository.findAll();
        // Then
        assertThat(reviews.size()).isGreaterThan(0);
        assertThat(reviews.get(0).getContent()).isEqualTo(content);
        assertThat(reviews.get(0).getRemoveYn()).isEqualTo("N");
        assertThat(photos.size()).isEqualTo(reviewRequestDto.getAttachedPhotoIds().size());
        assertThat(photos.get(0).getRemoveYn()).isEqualTo("N");

    }
}