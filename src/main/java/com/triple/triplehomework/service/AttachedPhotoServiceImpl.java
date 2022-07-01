package com.triple.triplehomework.service;

import com.triple.triplehomework.entity.review.AttachedPhoto;
import com.triple.triplehomework.entity.review.AttachedPhotoId;
import com.triple.triplehomework.entity.review.Review;
import com.triple.triplehomework.repository.AttachedPhotoRepository;
import com.triple.triplehomework.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttachedPhotoServiceImpl implements AttachedPhotoService{

    private final ReviewRepository reviewRepository;
    private final AttachedPhotoRepository photoRepository;

    @Override
    public void register(UUID reviewId, List<String> photoIds) {
        List<AttachedPhoto> photos = new ArrayList<>();

        Review review = reviewRepository.getReferenceById(reviewId);
        AttachedPhotoId attachedPhotoId = AttachedPhotoId.createAttachedPhotoId(review, 0L);

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
    public void remove() {

    }
}
