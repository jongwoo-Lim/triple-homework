package com.triple.triplehomework.service;

import com.triple.triplehomework.common.code.PointCode;
import com.triple.triplehomework.common.code.ReviewActionCode;
import com.triple.triplehomework.entity.point.Point;
import com.triple.triplehomework.entity.point.PointId;
import com.triple.triplehomework.entity.review.AttachedPhoto;
import com.triple.triplehomework.entity.review.Review;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PointServiceImplTest extends BaseServiceTest{

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
        assertThat(reviews.get(0).getAction()).isEqualTo(ReviewActionCode.ADD);
        assertThat(reviews.get(0).getRemoveYn()).isEqualTo("N");
        assertThat(photos.size()).isEqualTo(reviewRequestDto.getAttachedPhotoIds().size());
        assertThat(photos.get(0).getRemoveYn()).isEqualTo("N");

        assertThat(points.size()).isEqualTo(3);
        assertThat(point.getAccumAmt()).isEqualTo(1);
        assertThat(point.getBalAmt()).isEqualTo(3);
    }
}