package com.triple.triplehomework.service;

import com.triple.triplehomework.dto.TotalPointResponseDto;
import com.triple.triplehomework.entity.point.TotalPoint;

import java.util.UUID;

public interface MemberService {

    TotalPointResponseDto getTotalPoint(UUID userId);

    default TotalPointResponseDto entityToDto(TotalPoint memberPoint, int totalPoint){
        return TotalPointResponseDto.builder()
                .mno(memberPoint.getMno())
                .earnTotalAmt(memberPoint.getEarnTotalAmt())
                .deductTotalAmt(memberPoint.getDeductTotalAmt())
                .totalPoint(totalPoint)
                .build();
    }
}
