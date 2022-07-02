package com.triple.triplehomework.service;

import com.triple.triplehomework.entity.review.AttachedPhoto;
import com.triple.triplehomework.entity.review.AttachedPhotoId;
import com.triple.triplehomework.entity.review.Review;
import com.triple.triplehomework.repository.AttachedPhotoRepository;
import com.triple.triplehomework.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AttachedPhotoServiceImpl implements AttachedPhotoService{

    private final ReviewRepository reviewRepository;
    private final AttachedPhotoRepository photoRepository;


    @Override
    public void register(UUID reviewId, List<String> photoIds) {

        log.info("attached photo register...");

        List<AttachedPhoto> photos = new ArrayList<>();

        Review review = reviewRepository.getReferenceById(reviewId);
        AttachedPhotoId attachedPhotoId;

        // 기존 첨부파일이 있는 경우
        if(isAttached(review)){
            Pageable pageable = PageRequest.of(0, 1);
            AttachedPhoto point = photoRepository.findByReviewAndDesc(review, pageable).get(0);
            attachedPhotoId = point.getAttachedPhotoId();
        }else{
            attachedPhotoId = AttachedPhotoId.createAttachedPhotoId(review, 0L);
        }


        // 첨부파일 엔티티 생성
        for(String strPhotoId : photoIds){
            UUID photoId = UUID.fromString(strPhotoId);
            AttachedPhoto attachedPhoto = AttachedPhoto.createAttachedPhoto(attachedPhotoId, photoId);
            photos.add(attachedPhoto);
        }

        // 첨부파일 등록
        photoRepository.saveAll(photos);
    }

    @Override
    public boolean isAttached(UUID reviewId) {

        log.info("attached photo check...");
        Review review = reviewRepository.getReferenceById(reviewId);
        Pageable pageable = PageRequest.of(0, 1);
        List<AttachedPhoto> photos = photoRepository.findByReview(review, pageable);

        return photos.size() > 0;
    }

    @Override
    public boolean removeAll(UUID reviewId, List<UUID> photoIds) {

        log.info("attached photo remove...");
        Review review = reviewRepository.getReferenceById(reviewId);
        List<AttachedPhoto> photos = photoRepository.findByPhotoIds(review, photoIds);

        for(AttachedPhoto photo : photos){
            // 첨부파일 삭제
            photo.delete();
        }

        int count = photoRepository.countByReview(review, "N");
        return count == 0;
    }


    /**
     * 해당 리뷰의 첨부파일이 존재한 지 체크
     * @param review
     * @return
     */
    private boolean isAttached(Review review){
        Pageable pageable = PageRequest.of(0, 1);
        List<AttachedPhoto> photos = photoRepository.findByReview(review, pageable);

        return photos.size() > 0;
    }
}
