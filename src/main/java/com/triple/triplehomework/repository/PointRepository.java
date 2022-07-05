package com.triple.triplehomework.repository;

import com.triple.triplehomework.common.code.PointCode;
import com.triple.triplehomework.entity.point.Point;
import com.triple.triplehomework.entity.point.PointId;
import com.triple.triplehomework.entity.review.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PointRepository extends JpaRepository<Point, PointId> {

    @Query("select p.pointId " +
            "from Point p " +
            "where p.pointId.mno = :mno")
    Optional<PointId> findByPointId(@Param("mno") Long mno);

    @Query("select p " +
            "from Point p " +
            "where p.pointId.mno = :mno " +
            "order by p.pointId.occurSeq desc")
    List<Point> findByPoint(@Param("mno") Long mno, Pageable pageable);

    @Query("select p " +
            "from Point p " +
            "where p.pointId.mno = :mno")
    List<Point> existsPointByMno(@Param("mno") Long mno, Pageable pageable);

    @Query("select sum(p.accumAmt) " +
            "from Point p " +
            "where p.review = :review " +
            "and p.pointCode = :pointCode ")
    Integer countEarnPointByReview(@Param("review") Review review,@Param("pointCode") PointCode pointCode);

    @Query("select sum(p.accumAmt) " +
            "from Point p " +
            "where p.review = :review " +
            "and p.pointCode = :pointCode ")
    Integer countDeductPointByReview(@Param("review") Review review,@Param("pointCode") PointCode pointCode);
}
