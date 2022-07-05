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
@Transactional
public class ReviewServiceImpl implements ReviewService{

    // 삭제 여부 N
    private static final String NOT_REMOVED = "N";
    private final AttachedPhotoService photoService;
    private final PointService pointService;
    private final ReviewRepository reviewRepository;
    private final PlaceRepository placeRepository;

    @Override
    public ReviewResponseDto register(ReviewRequestDto reviewRequestDto) {

        log.info("Review register...");

        // 회원은 한 장소에 하나의 리뷰만 작성 가능
        final UUID placeId = UUID.fromString(reviewRequestDto.getPlaceId());
        final Place place = placeRepository.getReferenceById(placeId);

        final UUID userId = UUID.fromString(reviewRequestDto.getUserId());

        final boolean result = reviewRepository.existsReviewByUserIdAndPlaceAndRemoveYn(userId, place, NOT_REMOVED);

        if(result){
            // 요청한 회원은 리뷰 작성 기록이 있다
            throw new ReviewExistException("한 장소에 리뷰는 한 번만 작성하실 수 있습니다.");
        }

        // 해당 장소 첫 리뷰인지 체크
        final boolean isFirst = reviewRepository.existsReviewByPlace(place.getPlaceId(), NOT_REMOVED, PageRequest.of(0, 1)).size() == 0;

        // 리뷰 등록
        final Review review = Review.createReview(reviewRequestDto.getContent(), userId, place);
        final Review savedReview = reviewRepository.save(review);

        // 첨부파일이 있는 경우 첨부파일 등록
        final boolean attached = reviewRequestDto.getAttachedPhotoIds().size() > 0;

        if(attached){
           photoService.register(savedReview.getReviewId(), reviewRequestDto.getAttachedPhotoIds());
        }

        // 포인트 적립
        pointService.register(userId, savedReview.getReviewId(), attached, isFirst);

        final List<String> photos = photoService.getPhotoIds(savedReview.getReviewId());
        return entityToDto(review, photos);
    }

    @Override
    public ReviewResponseDto modify(ReviewRequestDto reviewRequestDto) {

        final UUID reviewId = UUID.fromString(reviewRequestDto.getReviewId());

        final Review review = reviewRepository.findByReviewIdAndRemoveYn(reviewId, NOT_REMOVED)
                .orElseThrow(() -> new ReviewNotFoundException("해당 리뷰는 존재하지 않습니다."));

        // 리뷰 내용 수정
        if(StringUtils.hasText(reviewRequestDto.getContent())){
            review.updateContent(reviewRequestDto.getContent());
        }

        final List<String> photoIds = reviewRequestDto.getAttachedPhotoIds();

        // 글만 작성한 리뷰에 사진 추가시 1점 적립
        if(photoIds != null && photoIds.size() > 0){

            final boolean attached = photoService.isAttached(review.getReviewId());

            // 첨부파일 추가
            photoService.register(reviewId, photoIds);

            if(!attached){
                // 포인트 적립
                pointService.registerPhotoPoint(review.getUserId(), review.getReviewId());
            }
        }

        final List<String> photos = photoService.getPhotoIds(reviewId);
        return entityToDto(review, photos);
    }

    @Override
    public boolean remove(ReviewRequestDto reviewRequestDto) {

        boolean result = false;
        final UUID reviewId = UUID.fromString(reviewRequestDto.getReviewId());
        final UUID userId = UUID.fromString(reviewRequestDto.getUserId());

        // 리뷰 조회
        final Review review = reviewRepository.findByReviewIdAndRemoveYn(reviewId, NOT_REMOVED)
                .orElseThrow(() -> new ReviewNotFoundException("해당 리뷰는 존재하지 않습니다."));

        final String removePhotoYn = reviewRequestDto.getRemovePhotoYn();
        final boolean removePhoto = StringUtils.hasText(removePhotoYn) && removePhotoYn.equalsIgnoreCase("Y");

        final List<UUID> photoIds = reviewRequestDto.getAttachedPhotoIds()
                .stream()
                .map(UUID::fromString).collect(Collectors.toList());

        if(removePhoto){
            // 첨부 파일 삭제시
            final boolean removedAll = photoService.removePhotos(reviewId, photoIds);
            // 모두 삭제 시 1점 회수
            if(removedAll){
                // 포인트 차감
                pointService.withdrawPhotoPoint(userId, reviewId);
            }

            result = true;
        }else{
            // 리뷰 삭제시
            // 해당 리뷰로 부여된 포인트 회수
            final boolean removedAll = photoService.removeAll(reviewId);
            if(removedAll){
                review.delete();
                pointService.withdrawReviewPoint(userId, reviewId);
                result = true;
            }
        }
        return result;
    }
}
