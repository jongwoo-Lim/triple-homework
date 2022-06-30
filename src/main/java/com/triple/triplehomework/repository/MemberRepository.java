package com.triple.triplehomework.repository;

import com.triple.triplehomework.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
