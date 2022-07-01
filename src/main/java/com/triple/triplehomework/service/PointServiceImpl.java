package com.triple.triplehomework.service;

import com.triple.triplehomework.common.code.PointCode;
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
            Point existingReviewPoint = Point.createPoint(pointId, PointCode.EARN,
                                                 1, point.getBalAmt() + 1,
                                                "리뷰 텍스트 작성", review);
            Point savedPoint = pointRepository.save(existingReviewPoint);

            // 1장 이상 사진 첨부시 포인트 1점 && 보너스 점수 1점
            if(attached && bonus){
                Point existingAttachedReviewPoint = Point.createPoint(pointId, PointCode.EARN,
                                                        1, savedPoint.getBalAmt() + 1,
                                                        "리뷰 사진 첨부", review);

                Point savedAttachedPoint = pointRepository.save(existingAttachedReviewPoint);

                Point bonusPoint = Point.createPoint(pointId, PointCode.EARN,
                                                        1, savedAttachedPoint.getBalAmt() + 1,
                                                        "첫 리뷰 보너스 점수", review);
                pointRepository.save(bonusPoint);
                return;
            }

            if(attached){
                // 1장 이상 사진 첨부시 포인트 1점
                Point existingAttachedReviewPoint = Point.createPoint(pointId, PointCode.EARN,
                                                            1, savedPoint.getBalAmt() + 1,
                                                            "리뷰 사진 첨부", review);
                pointRepository.save(existingAttachedReviewPoint);
            }
            if(bonus){
                // 특정 장소 첫 리뷰 작성시 포인트 1점
                Point bonusPoint = Point.createPoint(pointId, PointCode.EARN,
                                                    1, savedPoint.getBalAmt() + 1,
                                                    "첫 리뷰 보너스 점수", review);
                pointRepository.save(bonusPoint);
            }
        }else{
            // 기존 이력이 존재하지 않는 경우
            PointId pointId = PointId.createPointId(member.getMno(), 0L);
            Point reviewPoint = Point.createPoint(pointId, PointCode.EARN, 1, 1, "리뷰 텍스트 작성", review);
            Point savedPoint = pointRepository.save(reviewPoint);


            // 1장 이상 사진 첨부시 포인트 1점 && 보너즈 점수 1점
            if(attached && bonus){
                Point existingAttachedReviewPoint = Point.createPoint(pointId, PointCode.EARN,
                                                        1, savedPoint.getBalAmt() + 1,
                                                        "리뷰 사진 첨부", review);

                Point savedAttachedPoint = pointRepository.save(existingAttachedReviewPoint);

                Point bonusPoint = Point.createPoint(pointId, PointCode.EARN,
                                                        1, savedAttachedPoint.getBalAmt() + 1,
                                                        "첫 리뷰 보너스 점수", review);
                pointRepository.save(bonusPoint);
                return;
            }

            if(attached){
                // 1장 이상 사진 첨부시 포인트 1점
                Point existingAttachedReviewPoint = Point.createPoint(pointId, PointCode.EARN,
                                                            1, savedPoint.getBalAmt() + 1,
                                                            "리뷰 사진 첨부", review);
                pointRepository.save(existingAttachedReviewPoint);
            }

            if(bonus){
                // 특정 장소 첫 리뷰 작성시 포인트 1점
                Point bonusPoint = Point.createPoint(pointId, PointCode.EARN,
                                                            1, savedPoint.getBalAmt() + 1,
                                                            "첫 리뷰 보너스 점수", review);
                pointRepository.save(bonusPoint);
            }
        }
    }



}
