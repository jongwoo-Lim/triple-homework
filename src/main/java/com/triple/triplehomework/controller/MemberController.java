package com.triple.triplehomework.controller;

import com.triple.triplehomework.dto.ResponseDto;
import com.triple.triplehomework.dto.TotalPointResponseDto;
import com.triple.triplehomework.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final ResponseDto responseDto;

    @GetMapping("/member/{userId}/point")
    public ResponseEntity<?> memberPoint(@PathVariable String userId){

        if(StringUtils.hasText(userId)){
            final UUID uuid = UUID.fromString(userId);
            final TotalPointResponseDto pointBody = memberService.getTotalPoint(uuid);
            return responseDto.ok(pointBody, "회원 포인트 조회 성공", HttpStatus.OK);
        }

        return responseDto.badRequest("유저 아이디는 비어있을 수 없습니다.");
    }
}
