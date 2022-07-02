package com.triple.triplehomework.service;

import java.util.UUID;

public interface PointService {

    void register(UUID userId, UUID reviewId, boolean attached, boolean bonus);

    void registerPhotoPoint(UUID userId, UUID reviewId);

    void withdrawPhotoPoint(UUID userId, UUID reviewId);
}
