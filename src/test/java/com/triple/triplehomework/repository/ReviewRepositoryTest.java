package com.triple.triplehomework.repository;

import com.triple.triplehomework.entity.member.Member;
import com.triple.triplehomework.entity.place.Place;
import com.triple.triplehomework.entity.review.Review;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class ReviewRepositoryTest extends BaseRepositoryTest {

    @Test
    @DisplayName("유저가 첫 리뷰 작성인 지 체크 테스트")
    public void existsReviewByPlace_test() throws Exception{
        // Given
        Member admin = createMember();
        Place place = createPlace(admin);

        PageRequest pageable = PageRequest.of(0, 1);

        // When
        List<Object[]> reviews =
                reviewRepository.existsReviewByPlace(place.getPlaceId(), "N", pageable);

        boolean isFirst = reviews.size() == 0;

        // Then
        assertThat(isFirst).isTrue();
    }

    @Test
    @DisplayName("유저가 한 장소의 리뷰 0개 체크 테스트")
    public void countReview_zero_test() throws Exception{
        // Given
        Member admin = createMember();
        Place place = createPlace(admin);

        Member member = createMember();
        // When
        boolean result = reviewRepository.existsReviewByUserIdAndPlaceAndRemoveYn(member.getUserId(), place, "N");

        // Then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("유저가 한 장소의 리뷰 1개 체크 테스트")
    public void countReview_one_test() throws Exception{
        // Given
        Member admin = createMember();
        Place place = createPlace(admin);

        Member member = createMember();
        Review review = createReview(member, place);
        Review savedReview = reviewRepository.save(review);

        // When
        boolean result = reviewRepository.existsReviewByUserIdAndPlaceAndRemoveYn(member.getUserId(), place, "N");

        // Then
        assertThat(result).isTrue();
    }

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
        assertThat(savedReview.getReviewId()).isNotNull();
    }
}