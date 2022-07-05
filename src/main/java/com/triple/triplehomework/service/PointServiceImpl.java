package com.triple.triplehomework.service;

import com.triple.triplehomework.common.code.PointCode;
import com.triple.triplehomework.common.code.PointOccurCode;
import com.triple.triplehomework.entity.member.Member;
import com.triple.triplehomework.entity.point.Point;
import com.triple.triplehomework.entity.point.PointId;
import com.triple.triplehomework.entity.point.TotalPoint;
import com.triple.triplehomework.entity.review.Review;
import com.triple.triplehomework.exception.MemberNotFoundException;
import com.triple.triplehomework.repository.MemberRepository;
import com.triple.triplehomework.repository.PointRepository;
import com.triple.triplehomework.repository.ReviewRepository;
import com.triple.triplehomework.repository.TotalPointRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class PointServiceImpl implements PointService{

    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    private final PointRepository pointRepository;
    private final TotalPointRepository totalPointRepository;

    @Override
    public void register(UUID userId, UUID reviewId, boolean attached, boolean bonus) {

        log.info("point register...");

        final Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new MemberNotFoundException("해당 유저는 존재하지 않습니다."));

        final TotalPoint totalPoint = totalPointRepository.findById(member.getMno())
                .orElseGet(() -> TotalPoint.createTotalPoint(member.getMno(), 0, 0));

        final Review review = reviewRepository.getReferenceById(reviewId);

        // 1자 이상 텍스트 작성시 포인트 1점
        final Pageable pageable = PageRequest.of(0,1);
        final boolean existingPoint = pointRepository.existsPointByMno(member.getMno(), pageable).size() == 1;

        if(existingPoint){
            // 이력이 존재하는 경우
            final Point point = pointRepository.findByPoint(member.getMno(), pageable).get(0);

            final PointId pointId = PointId.createPointId(point.getPointId().getMno(), point.getPointId().getOccurSeq());
            final Point savedPoint = earnReviewPoint(review, pointId, point, PointOccurCode.REVIEW);
            totalPoint.increaseEarnTotalAmt();

            // 1장 이상 사진 첨부시 포인트 1점 && 보너스 점수 1점
            if(attached && bonus){
                earnPhotoAndBonusPoint(review, pointId, savedPoint, totalPoint);
                return;
            }

            if(attached){
                // 1장 이상 사진 첨부시 포인트 1점
                earnPhotoPoint(review, pointId, savedPoint, PointOccurCode.PHOTO);
                totalPoint.increaseEarnTotalAmt();
            }
            if(bonus){
                // 특정 장소 첫 리뷰 작성시 포인트 1점
                earnBonusPoint(review, pointId, savedPoint, PointOccurCode.BONUS);
                totalPoint.increaseEarnTotalAmt();
            }
        }else{
            // 기존 이력이 존재하지 않는 경우
            final PointId pointId = PointId.createPointId(member.getMno(), 0L);
            final Point savedPoint = earnReviewPoint(review, pointId, null, PointOccurCode.REVIEW);
            totalPoint.increaseEarnTotalAmt();

            // 1장 이상 사진 첨부시 포인트 1점 && 보너스 점수 1점
            if(attached && bonus){
                earnPhotoAndBonusPoint(review, pointId, savedPoint, totalPoint);
                return;
            }

            if(attached){
                // 1장 이상 사진 첨부시 포인트 1점
                earnPhotoPoint(review, pointId, savedPoint, PointOccurCode.PHOTO);
                totalPoint.increaseEarnTotalAmt();
            }

            if(bonus){
                // 특정 장소 첫 리뷰 작성시 포인트 1점
                earnBonusPoint(review, pointId, savedPoint, PointOccurCode.BONUS);
                totalPoint.increaseEarnTotalAmt();
            }
        }

        totalPointRepository.save(totalPoint);
    }

    @Override
    public void registerPhotoPoint(UUID userId, UUID reviewId) {

        log.info("photo point register...");

        // 유저 조회
        final Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new MemberNotFoundException("해당 유저는 존재하지 않습니다."));

        // 총 적립금액 조회
        final TotalPoint totalPoint = totalPointRepository.findById(member.getMno())
                .orElseGet(() -> TotalPoint.createTotalPoint(member.getMno(), 0, 0));

        final Review review = reviewRepository.getReferenceById(reviewId);

        // 포인트 최신 내역 조회
        final Pageable pageable = PageRequest.of(0,1);
        final List<Point> points = pointRepository.findByPoint(member.getMno(), pageable);

        if(points != null && points.size() >0){
            final Point point = points.get(0);
            final PointId pointId = PointId.createPointId(point.getPointId().getMno(), point.getPointId().getOccurSeq());

            // 사진 첨부시 포인트 1점
            earnPhotoPoint(review, pointId, point, PointOccurCode.PHOTO);
            totalPoint.increaseEarnTotalAmt();
        }
    }


    @Override
    public void withdrawPhotoPoint(UUID userId, UUID reviewId) {

        log.info("photo point withdraw...");

        // 유저 조회
        final Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new MemberNotFoundException("해당 유저는 존재하지 않습니다."));

        // 총 적립금액 조회
        final TotalPoint totalPoint = totalPointRepository.findById(member.getMno())
                .orElseGet(() -> TotalPoint.createTotalPoint(member.getMno(), 0, 0));

        final Review review = reviewRepository.getReferenceById(reviewId);

        // 포인트 최신 내역 조회
        final Pageable pageable = PageRequest.of(0,1);
        final List<Point> points = pointRepository.findByPoint(member.getMno(), pageable);

        if(points != null && points.size() > 0){
            // 내역이 있는 경우 점수 회수
            final Point point = points.get(0);
            final PointId pointId = PointId.createPointId(point.getPointId().getMno(), point.getPointId().getOccurSeq());

            // 포인트 1점 회수
            deductPhotoPoint(review, pointId, point, PointOccurCode.PHOTO_ALL_REMOVED);
            totalPoint.increaseDeductTotalAmt();
        }

    }

    @Override
    public void withdrawReviewPoint(UUID userId, UUID reviewId) {

        log.info("review point withdraw...");
        // 유저 조회
        final Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new MemberNotFoundException("해당 유저는 존재하지 않습니다."));

        // 총 적립금액 조회
        final TotalPoint totalPoint = totalPointRepository.findById(member.getMno())
                .orElseGet(() -> TotalPoint.createTotalPoint(member.getMno(), 0, 0));

        final Review review = reviewRepository.getReferenceById(reviewId);

        // 리뷰로 적립된 포인트 및 차감된 포인트 조회
        final int earnTotalPoint = ObjectUtils.defaultIfNull(pointRepository.countEarnPointByReview(review, PointCode.EARN),0);
        final int deductTotalPoint = ObjectUtils.defaultIfNull(pointRepository.countDeductPointByReview(review, PointCode.DEDUCT),0);

        // 포인트 최신 내역 조회
        final Pageable pageable = PageRequest.of(0,1);
        final List<Point> points = pointRepository.findByPoint(member.getMno(), pageable);

        if(points != null && points.size() > 0){
            // 내역이 있는 경우 점수 회수
            final Point point = points.get(0);
            final PointId pointId = PointId.createPointId(point.getPointId().getMno(), point.getPointId().getOccurSeq());

            final int deductPoint = earnTotalPoint - deductTotalPoint;

            // 리뷰로 부여받은 남은 포인트 회수
            deductReviewPoint(review, pointId, point, deductPoint, PointOccurCode.REVIEW_REMOVED);
            totalPoint.increaseDeductTotalAmt(deductPoint);
        }

    }

    /**
     * 리뷰 삭제시 부여받은 남은 포인트 회수
     * @param review
     * @param pointId
     * @param point
     * @param occurCode
     */
    private void deductReviewPoint(Review review, PointId pointId, Point point, int deductPoint, PointOccurCode occurCode) {

        final int balAmt = ObjectUtils.defaultIfNull(point.getBalAmt(),0);

        final Point reviewPoint =
                Point.createPoint(pointId, PointCode.DEDUCT, deductPoint, balAmt - deductPoint, occurCode.getOccurCause(), review);

        pointRepository.save(reviewPoint);
    }

    /**
     * 사진 삭제시 포인트 회수
     * @param review
     * @param pointId
     * @param point
     * @param occurCode
     */
    private void deductPhotoPoint(Review review, PointId pointId, Point point, PointOccurCode occurCode) {

        if(point != null && point.getBalAmt() != null){
            final Point photoPoint =
                    Point.createPoint(pointId, PointCode.DEDUCT, 1, point.getBalAmt() - 1, occurCode.getOccurCause(), review);
            pointRepository.save(photoPoint);
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
            final Point reviewPoint =
                    Point.createPoint(pointId, PointCode.EARN, 1, point.getBalAmt() + 1, occurCode.getOccurCause(), review);
            return pointRepository.save(reviewPoint);
        }

        final Point reviewPoint =
                Point.createPoint(pointId, PointCode.EARN, 1, 1, occurCode.getOccurCause(), review);
        return pointRepository.save(reviewPoint);
    }

    /**
     * 첨부파일 포인트 적립
     * @param review
     * @param pointId
     * @param savedPoint
     */
    private void earnPhotoPoint(Review review, PointId pointId, Point savedPoint, PointOccurCode occurCode) {
        final Point existingAttachedReviewPoint =
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
        final Point bonusPoint =
                Point.createPoint(pointId, PointCode.EARN, 1, savedPoint.getBalAmt() + 1, occurCode.getOccurCause(), review);
        pointRepository.save(bonusPoint);
    }

    /**
     * 첨부파일 && 보너스 점수 적립
     * 총 적립금액 적립
     * @param review
     * @param pointId
     * @param savedPoint
     */
    private void earnPhotoAndBonusPoint(Review review, PointId pointId, Point savedPoint, TotalPoint totalPoint) {
        final Point existingAttachedReviewPoint =
                Point.createPoint(pointId, PointCode.EARN, 1, savedPoint.getBalAmt() + 1, PointOccurCode.PHOTO.getOccurCause(), review);

        final Point savedAttachedPoint = pointRepository.save(existingAttachedReviewPoint);
        totalPoint.increaseEarnTotalAmt();

        earnBonusPoint(review, pointId, savedAttachedPoint, PointOccurCode.BONUS);
        totalPoint.increaseEarnTotalAmt();

        totalPointRepository.save(totalPoint);
    }


}
