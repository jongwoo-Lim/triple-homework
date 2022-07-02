package com.triple.triplehomework.service;

import java.util.List;
import java.util.UUID;

public interface AttachedPhotoService {

    // 등록
    void register(UUID reviewId, List<String> photoIds);

    // 해당 리뷰의 사진 있는 지 체크
    boolean isAttached(UUID reviewId);

    // 삭제
    boolean removeAll(UUID reviewId, List<UUID> photoIds);
}
