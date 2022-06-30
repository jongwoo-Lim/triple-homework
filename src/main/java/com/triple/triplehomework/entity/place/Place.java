package com.triple.triplehomework.entity.place;

import com.triple.triplehomework.entity.BaseEntity;
import com.triple.triplehomework.entity.member.Member;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "PLACE")
@NoArgsConstructor
@ToString(exclude = "member")
public class Place extends BaseEntity {

    @Id @GeneratedValue
    private Long pno;

    private UUID placeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mno")
    private Member member;

    public static Place createPlace(UUID placeId, Member member){
        return Place.builder()
                .placeId(placeId)
                .member(member)
                .build();
    }

    @Builder
    public Place(UUID placeId, Member member) {
        this.placeId = placeId;
        this.member = member;
    }
}
