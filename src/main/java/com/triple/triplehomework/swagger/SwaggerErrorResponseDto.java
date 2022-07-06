package com.triple.triplehomework.swagger;

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
@ApiModel(value = "API 에러 응답 정보")
public class SwaggerErrorResponseDto {

    @ApiModelProperty(value = "API 응답 상태", example = "400")
    private int status;
    @ApiModelProperty(value = "API 응답 메시지", example = "에러 정보 메시지")
    private String message;
    @ApiModelProperty(value = "API 응답 데이터")
    private Object data;
}
