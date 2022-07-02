package com.triple.triplehomework.service;

import com.triple.triplehomework.entity.review.AttachedPhoto;
import com.triple.triplehomework.entity.review.AttachedPhotoId;
import com.triple.triplehomework.entity.review.Review;
import com.triple.triplehomework.repository.AttachedPhotoRepository;
import com.triple.triplehomework.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttachedPhotoServiceImpl implements AttachedPhotoService{

    private final ReviewRepository reviewRepository;
    private final AttachedPhotoRepository photoRepository;

    @Transactional
    @Override
    public void register(UUID reviewId, List<String> photoIds) {

        List<AttachedPhoto> photos = new ArrayList<>();

        Review review = reviewRepository.getReferenceById(reviewId);
        AttachedPhotoId attachedPhotoId;

        // 기존 첨부파일이 있는 경우
        if(isAttached(review)){
            Pageable pageable = PageRequest.of(0, 1);
            AttachedPhoto point = photoRepository.findByPhotoAndDesc(review, pageable).get(0);
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

        Review review = reviewRepository.getReferenceById(reviewId);
        Pageable pageable = PageRequest.of(0, 1);
        List<AttachedPhoto> photos = photoRepository.findByPhoto(review, pageable);

        return photos.size() > 0;
    }

    @Override
    public void remove() {

    }

    /**
     * 해당 리뷰의 첨부파일이 존재한 지 체크
     * @param review
     * @return
     */
    private boolean isAttached(Review review){
        Pageable pageable = PageRequest.of(0, 1);
        List<AttachedPhoto> photos = photoRepository.findByPhoto(review, pageable);

        return photos.size() > 0;
    }
}
