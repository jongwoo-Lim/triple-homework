package com.triple.triplehomework.repository;

import com.triple.triplehomework.entity.point.Point;
import com.triple.triplehomework.entity.point.PointId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PointRepository extends JpaRepository<Point, PointId> {

    @Query("select p.pointId " +
            "from Point p " +
            "where p.pointId.mno = :mno")
    Optional<PointId> findByPointId(@Param("mno") Long mno);

    @Query("select p " +
            "from Point p " +
            "where p.pointId.mno = :mno")
    Optional<Point> findByPoint(@Param("mno") Long mno);
}
