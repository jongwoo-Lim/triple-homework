package com.triple.triplehomework.service;

import com.triple.triplehomework.common.code.PointCode;
import com.triple.triplehomework.common.code.ReviewActionCode;
import com.triple.triplehomework.dto.ReviewRequestDto;
import com.triple.triplehomework.dto.ReviewResponseDto;
import com.triple.triplehomework.entity.place.Place;
import com.triple.triplehomework.entity.point.Point;
import com.triple.triplehomework.entity.point.PointId;
import com.triple.triplehomework.entity.review.AttachedPhoto;
import com.triple.triplehomework.entity.review.Review;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PointServiceImplTest extends BaseServiceTest{

    @Test
    @DisplayName("글만 작성된 리뷰에서 사진 추가시 포인트 적립 테스트")
    public void registerPhotoPoint_test() throws Exception{
        //Given

        Place place = createPlace(member);
        String content = "review text..";
        ReviewRequestDto requestDto = createReviewRequestDto("", place, content, Collections.emptyList(), ReviewActionCode.ADD);

        ReviewResponseDto reviewResponseDto = reviewService.register(requestDto);
        Review review = reviewRepository.getReferenceById(UUID.fromString(reviewResponseDto.getReviewId()));
        int reviewPoint = pointRepository.countEarnPointByReview(review, PointCode.EARN);

        //When
        String photoId = "e4d1a64e-a531-46de-88d0-ff0ed70c0bb9";
        ReviewRequestDto updateRequestDto = createReviewRequestDto(reviewResponseDto.getReviewId(), place, content, List.of(photoId), ReviewActionCode.MOD);
        reviewService.modify(updateRequestDto);
        int totalPoint = pointRepository.countEarnPointByReview(review, PointCode.EARN);

        //Then
        assertThat(reviewPoint+1).isEqualTo(totalPoint);
    }

    @Test
    @DisplayName("리뷰 등록 후 포인트 내역 존재 적립(작성, 첨부파일, 보너스) 테스트")
    public void registerExistingPoint_test() throws Exception{
        // Given
        String content = "review test....";

        // 테스트 point 추가
        PointId pointId = PointId.createPointId(member.getMno(), 0L);
        Point test = Point.createPoint(pointId, PointCode.EARN, 1, 1, "test", null);
        pointRepository.save(test);

        // When
        reviewService.register(reviewRequestDto);

        List<Review> reviews = reviewRepository.findAll();
        List<AttachedPhoto> photos = attachedPhotoRepository.findAll();

        Pageable pageable = PageRequest.of(0,1);
        Point point = pointRepository.findByPoint(member.getMno(), pageable).get(0);

        List<Point> points = pointRepository.findAll();

        // Then
        assertThat(points.size()).isEqualTo(4);
        assertThat(point.getAccumAmt()).isEqualTo(1);
        assertThat(point.getBalAmt()).isEqualTo(4);
    }

    @Test
    @DisplayName("리뷰 등록 후 포인트 적립(작성, 첨부파일, 보너스) 테스트")
    public void registerPoint_test() throws Exception{
        // Given
        String content = "review test....";
        // When
        reviewService.register(reviewRequestDto);

        List<Review> reviews = reviewRepository.findAll();
        List<AttachedPhoto> photos = attachedPhotoRepository.findAll();

        Pageable pageable = PageRequest.of(0,1);
        Point point = pointRepository.findByPoint(member.getMno(), pageable).get(0);

        List<Point> points = pointRepository.findAll();
        // Then
        assertThat(reviews.size()).isGreaterThan(0);
        assertThat(reviews.get(0).getContent()).isEqualTo(content);
//        assertThat(reviews.get(0).getAction()).isEqualTo(ReviewActionCode.ADD);
        assertThat(reviews.get(0).getRemoveYn()).isEqualTo("N");
        assertThat(photos.size()).isEqualTo(reviewRequestDto.getAttachedPhotoIds().size());
        assertThat(photos.get(0).getRemoveYn()).isEqualTo("N");

        assertThat(points.size()).isEqualTo(3);
        assertThat(point.getAccumAmt()).isEqualTo(1);
        assertThat(point.getBalAmt()).isEqualTo(3);
    }




}