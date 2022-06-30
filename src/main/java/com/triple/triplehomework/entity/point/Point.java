package com.triple.triplehomework.entity.point;

import com.triple.triplehomework.common.code.PointCode;
import com.triple.triplehomework.entity.BaseEntity;
import com.triple.triplehomework.entity.review.Review;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "POINT")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString(exclude = "review")
public class Point extends BaseEntity {

    @EmbeddedId
    private PointId pointId;

    @Column(name = "point_cd")
    @Enumerated(EnumType.STRING)
    private PointCode pointCode;

    // 적립/차감 금액
    private Integer accumAmt;
    // 잔여금액
    private Integer balAmt;

    // 발생근거
    private String occurCause;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rno")
    private Review review;

    public static Point createPoint(
            PointId pointId, PointCode pointCode,
            Integer accumAmt, Integer balAmt,
            String occurCause, Review review){

        return Point.builder()
                .pointId(pointId)
                .pointCode(pointCode)
                .accumAmt(accumAmt)
                .balAmt(balAmt)
                .occurCause(occurCause)
                .review(review)
                .build();
    }

}
