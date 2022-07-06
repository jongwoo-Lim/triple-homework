package com.triple.triplehomework.controller;

import com.triple.triplehomework.dto.ResponseDto;
import com.triple.triplehomework.dto.TotalPointResponseDto;
import com.triple.triplehomework.service.MemberService;
import com.triple.triplehomework.swagger.SwaggerErrorResponseDto;
import com.triple.triplehomework.swagger.SwaggerTotalPointResponseDto;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

    @ApiResponses({
            @ApiResponse(code = 200, message = "여행자 포인트 조회 성공", response = SwaggerTotalPointResponseDto.class),
            @ApiResponse(code = 400, message = "여행자 포인트 조회 실패", response = SwaggerErrorResponseDto.class)
    })
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
