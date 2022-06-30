package com.triple.triplehomework.entity.point;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
public class PointId implements Serializable {

    // 회원번호
    private Long mno;
    // 발생순번
    private Long occurSeq;

    public static PointId createPointId(Long mno, Long occurSeq){
        return PointId.builder()
                .mno(mno)
                .occurSeq(occurSeq)
                .build();
    }

    /**
     * 발생 순번 증가
     */
    public void increaseOccurSeq(){
        this.occurSeq++;
    }
}
