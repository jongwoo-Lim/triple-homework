package com.triple.triplehomework.repository;

import com.triple.triplehomework.entity.place.Place;
import com.triple.triplehomework.entity.review.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

    boolean existsReviewByUserIdAndPlace(UUID userId, Place place);

    @Query("select p " +
            "from Place p " +
            "inner join Review r on p.placeId = r.place.placeId " +
            "where p.placeId = :placeId ")
    List<Place> existsReviewByPlace(@Param("placeId") UUID placeId, Pageable pageable);
}
