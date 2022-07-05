package com.triple.triplehomework.service;

import com.triple.triplehomework.dto.TotalPointResponseDto;
import com.triple.triplehomework.entity.member.Member;
import com.triple.triplehomework.entity.point.TotalPoint;
import com.triple.triplehomework.exception.MemberNotFoundException;
import com.triple.triplehomework.repository.MemberRepository;
import com.triple.triplehomework.repository.TotalPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final TotalPointRepository totalPointRepository;

    @Transactional(readOnly = true)
    @Override
    public TotalPointResponseDto getTotalPoint(UUID userId) {

        // 유저 조회
        final Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new MemberNotFoundException("해당 유저는 존재하지 않습니다."));

        // 회원 적립금 조회
        final TotalPoint memberPoint = totalPointRepository.findById(member.getMno())
                .orElseGet(() -> TotalPoint.createTotalPoint(member.getMno(), 0, 0));

        // 포인트 점수
        final int totalPoint = memberPoint.getEarnTotalAmt() - memberPoint.getDeductTotalAmt();

        return entityToDto(memberPoint, totalPoint);
    }
}
