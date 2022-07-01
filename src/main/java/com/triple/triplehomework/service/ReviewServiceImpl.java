package com.triple.triplehomework.service;

import com.triple.triplehomework.dto.ReviewRequestDto;
import com.triple.triplehomework.dto.ReviewResponseDto;
import com.triple.triplehomework.entity.place.Place;
import com.triple.triplehomework.entity.review.Review;
import com.triple.triplehomework.exception.ReviewExistException;
import com.triple.triplehomework.repository.PlaceRepository;
import com.triple.triplehomework.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{

    private final AttachedPhotoService photoService;
    private final ReviewRepository reviewRepository;
    private final PlaceRepository placeRepository;

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

        // 리뷰 등록
        Review review = Review.createReview(reviewRequestDto.getType(), reviewRequestDto.getAction(), reviewRequestDto.getContent(), userId, place);
        reviewRepository.save(review);

        // 첨부파일이 있는 경우 첨부파일 등록
        if(reviewRequestDto.getAttachedPhotoIds().size() > 0){
            photoService.register(review.getReviewId(), reviewRequestDto.getAttachedPhotoIds());
        }


        // 1자 이상 텍스트 작성시 포인트 1점
        // 1장 이상 사진 첨부시 포인트 1점
        // 특정 장소 첫 리뷰 작성시 포인트 1점
    }

    @Override
    public ReviewResponseDto modify(ReviewRequestDto reviewRequestDto) {
        return null;
    }

    @Override
    public boolean remove(ReviewRequestDto reviewRequestDto) {
        return false;
    }
}
