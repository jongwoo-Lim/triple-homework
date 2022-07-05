package com.triple.triplehomework.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TotalPointResponseDto {

    private long mno;
    private int earnTotalAmt;
    private int deductTotalAmt;
    private int totalPoint;
}
