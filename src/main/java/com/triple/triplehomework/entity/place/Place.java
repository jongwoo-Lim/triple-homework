package com.triple.triplehomework.entity.place;

import com.fasterxml.uuid.Generators;
import com.triple.triplehomework.entity.BaseEntity;
import com.triple.triplehomework.entity.member.Member;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "PLACE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(exclude = "member")
public class Place extends BaseEntity {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID placeId;

    // 등록자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mno")
    private Member member;

    @PrePersist
    public void createPlaceId() {
        if(this.placeId == null){
            //sequential uuid
            UUID uuid = Generators.timeBasedGenerator().generate();
            String[] uuidArr = uuid.toString().split("-");
            String uuidStr = uuidArr[2]+uuidArr[1]+uuidArr[0]+uuidArr[3]+uuidArr[4];
            StringBuffer sb = new StringBuffer(uuidStr);
            sb.insert(8, "-");
            sb.insert(13, "-");
            sb.insert(18, "-");
            sb.insert(23, "-");
            uuid = UUID.fromString(sb.toString());
            this.placeId = uuid;
        }

    }

    public static Place createPlace(Member member){
        return Place.builder()
                .member(member)
                .build();
    }

    @Builder
    public Place(UUID placeId, Member member) {
        this.placeId = placeId;
        this.member = member;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Place place = (Place) o;
        return placeId != null && Objects.equals(placeId, place.placeId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
