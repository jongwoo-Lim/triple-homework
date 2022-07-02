package com.triple.triplehomework.service;

import com.triple.triplehomework.entity.review.AttachedPhoto;
import com.triple.triplehomework.entity.review.Review;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class ReviewServiceImplTest extends BaseServiceTest {

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