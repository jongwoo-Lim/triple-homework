package com.triple.triplehomework.common.code;

public enum PointOccurCode {
    REVIEW("리뷰 작성"),
    PHOTO("사진 첨부"),
    BONUS("첫 리뷰 작성"),

    PHOTO_ALL_REMOVED("해당 리뷰 첨부 파일 모두 삭제"),
    REVIEW_REMOVED("해당 리뷰 삭제")
    ;

    private final String occurCause;

    PointOccurCode(String occurCause) {
        this.occurCause = occurCause;
    }

    public String getOccurCause() {
        return occurCause;
    }
}
