package com.triple.triplehomework.service;

import com.triple.triplehomework.dto.ReviewRequestDto;
import com.triple.triplehomework.dto.ReviewResponseDto;
import com.triple.triplehomework.entity.place.Place;
import com.triple.triplehomework.entity.review.Review;
import com.triple.triplehomework.exception.ReviewExistException;
import com.triple.triplehomework.exception.ReviewNotFoundException;
import com.triple.triplehomework.repository.PlaceRepository;
import com.triple.triplehomework.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{

    private final AttachedPhotoService photoService;
    private final PointService pointService;
    private final ReviewRepository reviewRepository;
    private final PlaceRepository placeRepository;

    @Transactional
    @Override
    public void register(ReviewRequestDto reviewRequestDto) {

        log.info("Review register...");

        // 회원은 한 장소에 하나의 리뷰만 작성 가능
        UUID placeId = UUID.fromString(reviewRequestDto.getPlaceId());
        Place place = placeRepository.getReferenceById(placeId);

        UUID userId = UUID.fromString(reviewRequestDto.getUserId());
        boolean result = reviewRepository.existsReviewByUserIdAndPlace(userId, place);

        if(result){
            // 요청한 회원은 리뷰 작성 기록이 있다
            throw new ReviewExistException("한 장소에 리뷰는 한 개만 작성할 수 있습니다.");
        }

        // 해당 장소 첫 리뷰인지 체크
        boolean isFirst = reviewRepository.existsReviewByPlace(place.getPlaceId(), "N", PageRequest.of(0, 1)).size() == 0;

        // 리뷰 등록
        Review review = Review.createReview(reviewRequestDto.getContent(), userId, place);
        reviewRepository.save(review);

        // 첨부파일이 있는 경우 첨부파일 등록
        boolean attached = reviewRequestDto.getAttachedPhotoIds().size() > 0;

        if(attached){
            photoService.register(review.getReviewId(), reviewRequestDto.getAttachedPhotoIds());
        }

        // 포인트 적립
        pointService.register(userId, review.getReviewId(), attached, isFirst);
    }

    @Transactional
    @Override
    public ReviewResponseDto modify(ReviewRequestDto reviewRequestDto) {

        UUID reviewId = UUID.fromString(reviewRequestDto.getReviewId());
        String removeYn = "N";

        Review review = reviewRepository.findByReviewIdAndRemoveYn(reviewId, removeYn)
                .orElseThrow(() -> new ReviewNotFoundException("해당 리뷰는 존재하지 않습니다."));

        // 리뷰 내용 수정
        review.updateContent(reviewRequestDto.getContent());

        List<String> photoIds = reviewRequestDto.getAttachedPhotoIds();

        // 글만 작성한 리뷰에 사진 추가시 1점 적립
        if(photoIds.size() > 0){

            boolean attached = photoService.isAttached(review.getReviewId());

            // 첨부파일 추가
            photoService.register(reviewId, photoIds);

            if(!attached){
                // 포인트 적립
                pointService.registerPhotoPoint(review.getUserId(), review.getReviewId());

            }

        }


        return null;
    }

    @Override
    public boolean remove(ReviewRequestDto reviewRequestDto) {

        boolean result;
        UUID reviewId = UUID.fromString(reviewRequestDto.getReviewId());
        UUID userId = UUID.fromString(reviewRequestDto.getUserId());
        String removeYn = "N";

        Review review = reviewRepository.findByReviewIdAndRemoveYn(reviewId, removeYn)
                .orElseThrow(() -> new ReviewNotFoundException("해당 리뷰는 존재하지 않습니다."));

        String removePhotoYn = reviewRequestDto.getRemovePhotoYn();
        boolean removePhoto = StringUtils.hasText(removePhotoYn) && removePhotoYn.equalsIgnoreCase("Y");

        List<UUID> photoIds = reviewRequestDto.getAttachedPhotoIds()
                .stream()
                .map(UUID::fromString).collect(Collectors.toList());

        if(removePhoto){

            // 첨부 파일 삭제시
            boolean removedAll = photoService.removeAll(reviewId, photoIds);
            // 모두 삭제 시 1점 회수
            if(removedAll){
                // 포인트 차감
                pointService.withdrawPhotoPoint(userId, reviewId);
            }

            result = true;
        }else{
            // 리뷰 삭제시
            // 해당 리뷰로 부여된 점수 회수
            review.delete();
            result = true;
        }

        return result;
    }
}
