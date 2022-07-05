package com.triple.triplehomework.entity.member;

import com.triple.triplehomework.entity.BaseEntity;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "MEMBER")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Member extends BaseEntity {

    @Id @GeneratedValue
    private Long mno;

    @Column(columnDefinition = "BINARY(16)", unique = true)
    private UUID userId;

    public static Member createMember(UUID userId){
        return Member.builder()
                .userId(userId)
                .build();
    }

    @Builder
    public Member(UUID userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Member member = (Member) o;
        return mno != null && Objects.equals(mno, member.mno);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
