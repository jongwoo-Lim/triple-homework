package com.triple.triplehomework.repository;

import com.triple.triplehomework.entity.member.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class MemberRepositoryTest extends BaseRepositoryTest{

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("유저 등록 테스트")
    public void createMember_test() throws Exception{
        // Given
        UUID userId = UUID.randomUUID();
        Member member = Member.createMember(userId);

        // When
        Member savedMember = memberRepository.save(member);

        memberRepository.findAll();
        // Then
        assertThat(savedMember.getUserId()).isEqualTo(userId);
        assertThat(savedMember.getMno()).isNotNull();
    }

}