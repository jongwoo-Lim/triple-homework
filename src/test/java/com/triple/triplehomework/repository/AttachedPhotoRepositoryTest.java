package com.triple.triplehomework.repository;

import com.triple.triplehomework.entity.member.Member;
import com.triple.triplehomework.entity.place.Place;
import com.triple.triplehomework.entity.review.AttachedPhoto;
import com.triple.triplehomework.entity.review.AttachedPhotoId;
import com.triple.triplehomework.entity.review.Review;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class AttachedPhotoRepositoryTest extends BaseRepositoryTest{

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

        AttachedPhotoId attachedPhotoId = attachedPhotoRepository.findByPhotoId(savedReview)
                .orElseGet(() -> AttachedPhotoId.createAttachedPhotoId(review, 0L));


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