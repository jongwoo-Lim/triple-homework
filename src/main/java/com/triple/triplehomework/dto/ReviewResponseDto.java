package com.triple.triplehomework.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "여행자 리뷰 응답 정보")
public class ReviewResponseDto {

    @ApiModelProperty(value = "리뷰아이디", example = "240a0658-dc5f-4878-9381-ebb7b2667772", required = true)
    private String reviewId;

    @ApiModelProperty(value = "리뷰내용", example = "좋아요!", required = true)
    private String content;

    @ApiModelProperty(value = "회원아이디", example = "3ede0ef2-92b7-4817-a5f3-0c575361f745", required = true)
    private String userId;

    @ApiModelProperty(value = "장소아이디", example = "2e4baf1c-5acb-4efb-a1af-eddada31b00f", required = true)
    private String placeId;

    @ApiModelProperty(value = "첨부파일 아이디", example = "[\"e4d1a64e-a531-46de-88d0-ff0ed70c0bb8\", \"afb0cef2-851d-4a50-bb07-9cc15cbdc332\"]")
    private List<String> attachedPhotoIds;

    @ApiModelProperty(value = "리뷰 삭제 여부", example = "N", required = true)
    private String removeYn;

    @ApiModelProperty(value = "등록일시", example = "2022-07-06T12:55:33.9935849", required = true)
    private LocalDateTime regDate;

    @ApiModelProperty(value = "수정일시", example = "2022-07-06T12:55:33.9935849", required = true)
    private LocalDateTime updDate;

}
