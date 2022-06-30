package com.triple.triplehomework.entity.member;

import com.triple.triplehomework.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "MEMBER")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Member extends BaseEntity {

    @Id @GeneratedValue
    private Long mno;
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
}
