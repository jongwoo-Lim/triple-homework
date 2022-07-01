package com.triple.triplehomework.entity.point;

import com.triple.triplehomework.common.code.PointCode;
import com.triple.triplehomework.entity.BaseEntity;
import com.triple.triplehomework.entity.review.Review;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "POINT")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
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
    @JoinColumn(name = "review_id")
    private Review review;

    public static Point createPoint(
            PointId pointId, PointCode pointCode,
            Integer accumAmt, Integer balAmt,
            String occurCause, Review review){

        pointId.increaseOccurSeq();
        PointId build = PointId.createPointId(pointId.getMno(), pointId.getOccurSeq());
        return Point.builder()
                .pointId(build)
                .pointCode(pointCode)
                .accumAmt(accumAmt)
                .balAmt(balAmt)
                .occurCause(occurCause)
                .review(review)
                .build();
    }

    /**
     * 적립금 및 잔여금액 증가
     */
    public void increaseAccumAmt(){
        this.accumAmt++;
        this.balAmt++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Point point = (Point) o;
        return pointId != null && Objects.equals(pointId, point.pointId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pointId);
    }
}
