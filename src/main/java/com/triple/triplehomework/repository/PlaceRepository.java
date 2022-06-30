package com.triple.triplehomework.repository;

import com.triple.triplehomework.entity.place.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long> {
}
