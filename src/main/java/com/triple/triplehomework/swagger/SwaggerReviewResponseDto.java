package com.triple.triplehomework.swagger;

import com.triple.triplehomework.dto.ReviewResponseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "여행자 리뷰 API 응답 정보")
public class SwaggerReviewResponseDto {

    @ApiModelProperty(value = "API 응답 상태", example = "200")
    private int status;
    @ApiModelProperty(value = "API 응답 메시지", example = "리뷰 작성,수정,삭제 성공")
    private String message;
    @ApiModelProperty(value = "API 응답 데이터")
    private ReviewResponseDto data;
}
