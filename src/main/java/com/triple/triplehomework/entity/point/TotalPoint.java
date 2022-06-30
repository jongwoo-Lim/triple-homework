package com.triple.triplehomework.entity.point;

import com.triple.triplehomework.entity.BaseEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TOTAL_POINT")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
public class TotalPoint extends BaseEntity {

    @Id
    private Long mno;

    // 적립총액
    private Integer accumTotalAmt;

    // 차감총액
    private Integer balTotalAmt;

    public static TotalPoint createTotalPoint(Long mno, Integer accumTotalAmt, Integer balTotalAmt){
        return TotalPoint.builder()
                .mno(mno)
                .accumTotalAmt(accumTotalAmt)
                .balTotalAmt(balTotalAmt)
                .build();
    }
}
