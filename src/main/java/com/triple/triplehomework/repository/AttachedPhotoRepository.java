package com.triple.triplehomework.repository;

import com.triple.triplehomework.entity.review.AttachedPhoto;
import com.triple.triplehomework.entity.review.AttachedPhotoId;
import com.triple.triplehomework.entity.review.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface AttachedPhotoRepository extends JpaRepository<AttachedPhoto, AttachedPhotoId> {

    @Query("select p " +
            "from AttachedPhoto p " +
            "where p.attachedPhotoId.review = :review " +
            "and p.removeYn = :removeYn")
    List<AttachedPhoto> findByReview(@Param("review") Review review, @Param("removeYn") String removeYn);

    @Query("select p " +
            "from AttachedPhoto p " +
            "where p.attachedPhotoId.review = :review ")
    List<AttachedPhoto> findByReview(@Param("review") Review review, Pageable pageable);

    @Query("select p " +
            "from AttachedPhoto p " +
            "where p.attachedPhotoId.review = :review " +
            "and p.removeYn = :removeYn")
    List<AttachedPhoto> findByReview(@Param("review") Review review, @Param("removeYn") String removeYn, Pageable pageable);


    @Query("select p " +
            "from AttachedPhoto p " +
            "where p.attachedPhotoId.review = :review " +
            "order by p.regDate desc")
    List<AttachedPhoto> findByReviewAndDesc(@Param("review") Review review, Pageable pageable);

    @Query("select p " +
            "from AttachedPhoto p " +
            "where p.attachedPhotoId.review = :review " +
            "and p.removeYn = :removeYn " +
            "and p.photoId in :photoIds ")
    List<AttachedPhoto> findByPhotoIds(@Param("review") Review review, @Param("photoIds") List<UUID> photoIds, @Param("removeYn") String removeYn);

    @Query("select count(p) " +
            "from AttachedPhoto p " +
            "where p.attachedPhotoId.review = :review " +
            "and p.removeYn = :removeYn")
    int countByReview(@Param("review") Review review, @Param("removeYn") String removeYn);
}
