package com.triple.triplehomework.repository;

import com.triple.triplehomework.entity.member.Member;
import com.triple.triplehomework.entity.place.Place;
import com.triple.triplehomework.entity.review.AttachedPhoto;
import com.triple.triplehomework.entity.review.AttachedPhotoId;
import com.triple.triplehomework.entity.review.Review;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class AttachedPhotoRepositoryTest extends BaseRepositoryTest{

    @Test
    @DisplayName("첨부파일 ID 조회 테스트")
    public void findByPhotoIds() throws Exception{
        //Given
        Member admin = createMember();
        Place place = createPlace(admin);
        Member member = createMember();
        Review review = createReview(member, place);
        Review savedReview = reviewRepository.save(review);

        List<UUID> photoIds = new ArrayList<>();
        List<AttachedPhoto> photos = new ArrayList<>();

        for(int i=0; i<3; i++){
            photoIds.add(UUID.randomUUID());
        }

        AttachedPhotoId attachedPhotoId;
        List<AttachedPhoto> attachedPhotos = attachedPhotoRepository.findByReviewAndDesc(savedReview, PageRequest.of(0, 1));

        if(attachedPhotos != null && attachedPhotos.size()>0){
            attachedPhotoId= attachedPhotos.get(0).getAttachedPhotoId();
        }else{
            attachedPhotoId = AttachedPhotoId.createAttachedPhotoId(review, 0L);
        }

        for(UUID photo : photoIds){
            final AttachedPhoto attachedPhoto = AttachedPhoto.createAttachedPhoto(attachedPhotoId, photo);
            photos.add(attachedPhoto);
        }

        // When
        attachedPhotoRepository.saveAll(photos);
        //When
        List<AttachedPhoto> existingPhotos = attachedPhotoRepository.findByPhotoIds(savedReview, photoIds, NOT_REMOVED);

        //ThenR
        assertThat(existingPhotos.size()).isEqualTo(photoIds.size());
    }
    @Test
    @DisplayName("리뷰 첨부파일 등록 테스트")
    public void createAttachedPhoto_test() throws Exception{
        // Given
        Member admin = createMember();
        Place place = createPlace(admin);
        Member member = createMember();
        Review review = createReview(member, place);
        Review savedReview = reviewRepository.save(review);

//        reviewRepository.findAll();
        UUID photoId1 = UUID.randomUUID();
        UUID photoId2 = UUID.randomUUID();
        UUID photoId3 = UUID.randomUUID();

        AttachedPhotoId attachedPhotoId;
        List<AttachedPhoto> attachedPhotos = attachedPhotoRepository.findByReviewAndDesc(savedReview, PageRequest.of(0, 1));

        if(attachedPhotos != null && attachedPhotos.size()>0){
            attachedPhotoId= attachedPhotos.get(0).getAttachedPhotoId();
        }else{
            attachedPhotoId = AttachedPhotoId.createAttachedPhotoId(review, 0L);
        }

        AttachedPhoto attachedPhoto1 = AttachedPhoto.createAttachedPhoto(attachedPhotoId, photoId1);
        AttachedPhoto attachedPhoto2 = AttachedPhoto.createAttachedPhoto(attachedPhotoId, photoId2);
        AttachedPhoto attachedPhoto3 = AttachedPhoto.createAttachedPhoto(attachedPhotoId, photoId3);

        // When
        attachedPhotoRepository.save(attachedPhoto1);
        attachedPhotoRepository.save(attachedPhoto2);
        attachedPhotoRepository.save(attachedPhoto3);

        List<AttachedPhoto> photos = attachedPhotoRepository.findAll();
        photos.forEach(System.out::println);

        // Then
        assertThat(photos.size()).isGreaterThan(0);
        assertThat(photos.size()).isEqualTo(3);
    }

}