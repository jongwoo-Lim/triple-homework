package com.triple.triplehomework.swagger;

import com.triple.triplehomework.dto.TotalPointResponseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(value = "여행자 포인트 API 응답 정보")
public class SwaggerTotalPointResponseDto {

    @ApiModelProperty(value = "API 응답 상태", example = "200")
    private int status;
    @ApiModelProperty(value = "API 응답 메시지", example = "회원 적립금 조회 성공")
    private String message;
    @ApiModelProperty(value = "API 응답 데이터")
    private TotalPointResponseDto data;
}
