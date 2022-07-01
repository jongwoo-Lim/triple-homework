package com.triple.triplehomework.repository;

import com.triple.triplehomework.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUserId(UUID userId);
}
