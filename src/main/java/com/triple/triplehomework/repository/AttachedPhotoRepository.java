package com.triple.triplehomework.repository;

import com.triple.triplehomework.entity.review.AttachedPhoto;
import com.triple.triplehomework.entity.review.AttachedPhotoId;
import com.triple.triplehomework.entity.review.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AttachedPhotoRepository extends JpaRepository<AttachedPhoto, AttachedPhotoId> {

    @Query("select p.attachedPhotoId " +
            "from AttachedPhoto p " +
            "where p.attachedPhotoId.review = :review ")
    Optional<AttachedPhotoId> findByPhotoId(@Param("review") Review review);


    @Query("select p " +
            "from AttachedPhoto p " +
            "where p.attachedPhotoId.review = :review ")
    List<AttachedPhoto> findByPhoto(@Param("review") Review review, Pageable pageable);


    @Query("select p " +
            "from AttachedPhoto p " +
            "where p.attachedPhotoId.review = :review " +
            "order by p.regDate desc")
    List<AttachedPhoto> findByPhotoAndDesc(@Param("review") Review review, Pageable pageable);
}
