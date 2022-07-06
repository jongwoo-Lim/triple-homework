package com.triple.triplehomework.dto;

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
@ApiModel(description = "여행자 포인트 적립 조회 응답 정보")
public class TotalPointResponseDto {

    @ApiModelProperty(value = "회원아이디", example = "3ede0ef2-92b7-4817-a5f3-0c575361f745")
    private long mno;

    @ApiModelProperty(value = "포인트 총 적립금액", example = "6")
    private int earnTotalAmt;

    @ApiModelProperty(value = "포인트 총 차감금액", example = "0")
    private int deductTotalAmt;

    @ApiModelProperty(value = "포인트 잔여금액", example = "6")
    private int totalPoint;
}
