package com.triple.triplehomework.repository;

import com.triple.triplehomework.entity.place.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PlaceRepository extends JpaRepository<Place, UUID> {
}
