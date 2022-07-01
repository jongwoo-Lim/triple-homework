package com.triple.triplehomework.service;

import com.triple.triplehomework.dto.ReviewRequestDto;
import com.triple.triplehomework.dto.ReviewResponseDto;

public interface ReviewService {

    // 리뷰 작성
    void register(ReviewRequestDto reviewRequestDto);
    // 리뷰 수정
    ReviewResponseDto modify(ReviewRequestDto reviewRequestDto);
    // 리뷰 삭제
    boolean remove(ReviewRequestDto reviewRequestDto);
}
