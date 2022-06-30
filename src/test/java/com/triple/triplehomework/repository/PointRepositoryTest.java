package com.triple.triplehomework.repository;

import com.triple.triplehomework.common.code.PointCode;
import com.triple.triplehomework.entity.member.Member;
import com.triple.triplehomework.entity.place.Place;
import com.triple.triplehomework.entity.point.Point;
import com.triple.triplehomework.entity.point.PointId;
import com.triple.triplehomework.entity.point.TotalPoint;
import com.triple.triplehomework.entity.review.AttachedPhoto;
import com.triple.triplehomework.entity.review.AttachedPhotoId;
import com.triple.triplehomework.entity.review.Review;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class PointRepositoryTest extends BaseRepositoryTest{

    @Test
    @DisplayName("회원 적립금 등록 테스트")
    public void createTotalPoint_test() throws Exception{
        // Given
        Member admin = createMember();
        Place place = createPlace(admin);

        // 유저
        Member member = createMember();
        // 리뷰
        Review review = createReview(member, place);
        Review savedReview = reviewRepository.save(review);
        // 첨부 파일
        createAttachedPhoto(savedReview);

        // 포인트
        PointId pointId = pointRepository.findByPointId(member.getMno())
                .orElseGet(() -> PointId.createPointId(member.getMno(), 0L));
        Point point = Point.createPoint(pointId, PointCode.EARN, 1, 1, "리뷰", review);
        Point savedPoint = pointRepository.save(point);
//        pointRepository.findAll();

        // When
        TotalPoint totalPoint = TotalPoint.createTotalPoint(member.getMno(), point.getAccumAmt(), 0);
        TotalPoint savedTotalPoint = totalPointRepository.save(totalPoint);

        totalPointRepository.findAll();
        // Then
        assertThat(savedTotalPoint).isNotNull();
        assertThat(savedTotalPoint.getEarnTotalAmt()).isEqualTo(point.getAccumAmt());
    }

    @Test
    @DisplayName("포인트 발생 내역 존재 시 등록 테스트")
    public void existingPoint_test() throws Exception{
        // Given
        Member admin = createMember();
        Place place = createPlace(admin);

        // 유저
        Member member = createMember();
        // 리뷰
        Review review = createReview(member, place);
        Review savedReview = reviewRepository.save(review);

        // 포인트
        PointId pointId = pointRepository.findByPointId(member.getMno())
                .orElseGet(() -> PointId.createPointId(member.getMno(), 0L));
        Point point = Point.createPoint(pointId, PointCode.EARN, 1, 1, "리뷰", review);
        Point savedPoint = pointRepository.save(point);
        pointRepository.findAll();


        Point existingPoint = pointRepository.findByPoint(member.getMno())
                .orElseThrow(() -> new IllegalArgumentException("해당 회원의 포인트 발생 내역이 없습니다."));

        Integer existingAccumAmt = existingPoint.getAccumAmt();
        Integer existingBalAmt = existingPoint.getBalAmt();
        Long existingOccurSeq = existingPoint.getPointId().getOccurSeq();

        // When
        // 첨부파일 추가 시 포인트 1 적립
        createAttachedPhoto(savedReview);

        Point point2 = Point.createPoint(existingPoint.getPointId(), PointCode.EARN, existingPoint.getAccumAmt(), existingPoint.getBalAmt(), "첨부 파일", review);
        point2.increaseAccumAmt();

        pointRepository.save(point2);
        pointRepository.findAll();

        // Then
        assertThat(point2.getPointId().getOccurSeq()).isEqualTo(existingOccurSeq + 1);
        assertThat(point2.getAccumAmt()).isEqualTo(existingAccumAmt + 1);
        assertThat(point2.getBalAmt()).isEqualTo(existingBalAmt + 1);
    }

    @Test
    @DisplayName("포인트 발생 내역 등록 테스트")
    public void createPoint_test() throws Exception{
        // Given
        Member admin = createMember();
        Place place = createPlace(admin);
        Member member = createMember();
        Review review = createReview(member, place);
        Review savedReview = reviewRepository.save(review);

        reviewRepository.findAll();

        PointId pointId = pointRepository.findByPointId(member.getMno())
                .orElseGet(() -> PointId.createPointId(member.getMno(), 0L));

        Point point = Point.createPoint(pointId, PointCode.EARN, 1, 1, "리뷰", review);

        // When
        Point savedPoint = pointRepository.save(point);
        pointRepository.findAll();

        // Then
        assertThat(savedPoint.getPointId().getOccurSeq()).isEqualTo(1);
        assertThat(savedPoint.getPointCode()).isEqualTo(PointCode.EARN);
    }


    /**
     * 리뷰에 대한 첨부파일 추가
     * @param review
     */
    private void createAttachedPhoto(Review review){
        UUID photoId1 = UUID.randomUUID();

        AttachedPhotoId attachedPhotoId = attachedPhotoRepository.findByPhotoId(review)
                .orElseGet(() -> AttachedPhotoId.createAttachedPhotoId(review, 0L));

        AttachedPhoto attachedPhoto1 = AttachedPhoto.createAttachedPhoto(attachedPhotoId, photoId1);

        attachedPhotoRepository.save(attachedPhoto1);
    }
}