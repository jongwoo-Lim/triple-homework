package com.triple.triplehomework.service;

import com.triple.triplehomework.common.code.PointCode;
import com.triple.triplehomework.common.code.PointOccurCode;
import com.triple.triplehomework.entity.member.Member;
import com.triple.triplehomework.entity.point.Point;
import com.triple.triplehomework.entity.point.PointId;
import com.triple.triplehomework.entity.review.Review;
import com.triple.triplehomework.repository.MemberRepository;
import com.triple.triplehomework.repository.PointRepository;
import com.triple.triplehomework.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService{

    private final PointRepository pointRepository;
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;

    @Transactional
    @Override
    public void register(UUID userId, UUID reviewId, boolean attached, boolean bonus) {
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저는 존재하지 않습니다."));

        Review review = reviewRepository.getReferenceById(reviewId);

        // 1자 이상 텍스트 작성시 포인트 1점
        Pageable pageable = PageRequest.of(0,1);
        boolean existingPoint = pointRepository.existsPointByMno(member.getMno(), pageable).size() == 1;

        if(existingPoint){
            // 이력이 존재하는 경우
            Point point = pointRepository.findByPoint(member.getMno(), pageable).get(0);

            PointId pointId = PointId.createPointId(point.getPointId().getMno(), point.getPointId().getOccurSeq());
            Point savedPoint = earnReviewPoint(review, pointId, point, PointOccurCode.REVIEW);

            // 1장 이상 사진 첨부시 포인트 1점 && 보너스 점수 1점
            if(attached && bonus){
                earnPhotoAndBonusPoint(review, pointId, savedPoint);
                return;
            }

            if(attached){
                // 1장 이상 사진 첨부시 포인트 1점
                eranPhotoPoint(review, pointId, savedPoint, PointOccurCode.PHOTO);
            }
            if(bonus){
                // 특정 장소 첫 리뷰 작성시 포인트 1점
                earnBonusPoint(review, pointId, savedPoint, PointOccurCode.BONUS);
            }
        }else{
            // 기존 이력이 존재하지 않는 경우
            PointId pointId = PointId.createPointId(member.getMno(), 0L);
            Point savedPoint = earnReviewPoint(review, pointId, null, PointOccurCode.REVIEW);

            // 1장 이상 사진 첨부시 포인트 1점 && 보너즈 점수 1점
            if(attached && bonus){
                earnPhotoAndBonusPoint(review, pointId, savedPoint);
                return;
            }

            if(attached){
                // 1장 이상 사진 첨부시 포인트 1점
                eranPhotoPoint(review, pointId, savedPoint, PointOccurCode.PHOTO);
            }

            if(bonus){
                // 특정 장소 첫 리뷰 작성시 포인트 1점
                earnBonusPoint(review, pointId, savedPoint, PointOccurCode.BONUS);
            }
        }
    }


    /**
     * 리뷰 포인트 적립
     * @param review
     * @param pointId
     * @param point
     * @param occurCode
     * @return
     */
    private Point earnReviewPoint(Review review, PointId pointId, Point point, PointOccurCode occurCode){

        if(point != null && point.getBalAmt() != null){
            Point reviewPoint =
                    Point.createPoint(pointId, PointCode.EARN, 1, point.getBalAmt() + 1, occurCode.getOccurCause(), review);
            return pointRepository.save(reviewPoint);
        }

        Point reviewPoint =
                Point.createPoint(pointId, PointCode.EARN, 1, 1, occurCode.getOccurCause(), review);
        return pointRepository.save(reviewPoint);
    }

    /**
     * 첨부파일 포인트 적립
     * @param review
     * @param pointId
     * @param savedPoint
     */
    private void eranPhotoPoint(Review review, PointId pointId, Point savedPoint, PointOccurCode occurCode) {
        Point existingAttachedReviewPoint =
                Point.createPoint(pointId, PointCode.EARN, 1, savedPoint.getBalAmt() + 1, occurCode.getOccurCause(), review);
        pointRepository.save(existingAttachedReviewPoint);
    }


    /**
     * 보너스 점수 적립
     * @param review
     * @param pointId
     * @param savedPoint
     */
    private void earnBonusPoint(Review review, PointId pointId, Point savedPoint, PointOccurCode occurCode) {
        Point bonusPoint =
                Point.createPoint(pointId, PointCode.EARN, 1, savedPoint.getBalAmt() + 1, occurCode.getOccurCause(), review);
        pointRepository.save(bonusPoint);
    }

    /**
     * 첨부파일 && 보너스 점수 적립
     * @param review
     * @param pointId
     * @param savedPoint
     */
    private void earnPhotoAndBonusPoint(Review review, PointId pointId, Point savedPoint) {
        Point existingAttachedReviewPoint =
                Point.createPoint(pointId, PointCode.EARN, 1, savedPoint.getBalAmt() + 1, PointOccurCode.PHOTO.getOccurCause(), review);

        Point savedAttachedPoint = pointRepository.save(existingAttachedReviewPoint);

        earnBonusPoint(review, pointId, savedAttachedPoint, PointOccurCode.BONUS);
    }


}