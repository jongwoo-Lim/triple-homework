package com.triple.triplehomework.repository;

import com.triple.triplehomework.common.code.ReviewActionCode;
import com.triple.triplehomework.entity.member.Member;
import com.triple.triplehomework.entity.place.Place;
import com.triple.triplehomework.entity.review.Review;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class ReviewRepositoryTest extends BaseRepositoryTest {

    @Test
    @DisplayName("리뷰 등록 테스트")
    public void createReview_test() throws Exception{
        // Given
        Member admin = createMember();
        Place place = createPlace(admin);

        Member member = createMember();
        Review review = createReview(member, place);
        // When
        Review savedReview = reviewRepository.save(review);
        reviewRepository.findAll();
        // Then
        assertThat(savedReview.getRno()).isNotNull();
        assertThat(savedReview.getAction()).isEqualTo(ReviewActionCode.ADD);
    }

}