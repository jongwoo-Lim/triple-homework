package com.triple.triplehomework.service;

import com.triple.triplehomework.common.code.ReviewActionCode;
import com.triple.triplehomework.dto.ReviewRequestDto;
import com.triple.triplehomework.entity.member.Member;
import com.triple.triplehomework.entity.place.Place;
import com.triple.triplehomework.entity.review.AttachedPhoto;
import com.triple.triplehomework.entity.review.Review;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class ReviewServiceImplTest extends BaseServiceTest {

    private ReviewRequestDto reviewRequestDto;

    @BeforeEach
    public void setUp(){
        Member admin = createMember();
        Place place = createPlace(admin);
        Member member = createMember();

        reviewRequestDto = ReviewRequestDto.builder()
                .type("REVIEW")
                .action(ReviewActionCode.ADD)
                .content("review test....")
                .attachedPhotoIds(List.of("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"))
                .userId(member.getUserId().toString())
                .placeId(place.getPlaceId().toString())
                .build();
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
        assertThat(reviews.get(0).getAction()).isEqualTo(ReviewActionCode.ADD);
        assertThat(reviews.get(0).getRemoveYn()).isEqualTo("N");
        assertThat(photos.size()).isEqualTo(reviewRequestDto.getAttachedPhotoIds().size());
        assertThat(photos.get(0).getRemoveYn()).isEqualTo("N");

    }
}