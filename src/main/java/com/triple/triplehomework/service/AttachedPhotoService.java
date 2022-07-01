package com.triple.triplehomework.service;

import java.util.List;
import java.util.UUID;

public interface AttachedPhotoService {

    // 등록
    void register(UUID reviewId, List<String> photoIds);

    // 삭제
    void remove();
}
