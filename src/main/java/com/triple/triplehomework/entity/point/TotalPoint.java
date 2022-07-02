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
@Getter
@ToString
public class TotalPoint extends BaseEntity {

    @Id
    private Long mno;

    // 적립총액
    private Integer earnTotalAmt;

    // 차감총액
    private Integer deductTotalAmt;

    public static TotalPoint createTotalPoint(Long mno, Integer earnTotalAmt, Integer deductTotalAmt){
        return TotalPoint.builder()
                .mno(mno)
                .earnTotalAmt(earnTotalAmt)
                .deductTotalAmt(deductTotalAmt)
                .build();
    }

    public void increaseEarnTotalAmt(){
        this.earnTotalAmt++;
    }

    public void increaseDeductTotalAmt(){
        this.deductTotalAmt++;
    }

    public void increaseDeductTotalAmt(int deductTotalAmt){
        this.deductTotalAmt += deductTotalAmt;
    }
}
