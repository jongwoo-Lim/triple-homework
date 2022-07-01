package com.triple.triplehomework.service;

import com.triple.triplehomework.common.code.ReviewActionCode;
import com.triple.triplehomework.dto.ReviewRequestDto;
import com.triple.triplehomework.entity.member.Member;
import com.triple.triplehomework.entity.place.Place;
import com.triple.triplehomework.entity.review.Review;
import com.triple.triplehomework.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

@SpringBootTest
@Disabled
public class BaseServiceTest {

    @Autowired
    protected ReviewService reviewService;

    @Autowired
    protected ReviewRepository repository;

    @Autowired
    protected PlaceRepository placeRepository;

    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected ReviewRepository reviewRepository;

    @Autowired
    protected AttachedPhotoRepository attachedPhotoRepository;

    @Autowired
    protected PointRepository pointRepository;

    @Autowired
    protected TotalPointRepository totalPointRepository;

    protected ReviewRequestDto reviewRequestDto;
    protected Member member;
    @BeforeEach
    public void setUp(){
        Member admin = createMember();
        Place place = createPlace(admin);
        member = createMember();

        reviewRequestDto = ReviewRequestDto.builder()
                .type("REVIEW")
                .action(ReviewActionCode.ADD)
                .content("review test....")
                .attachedPhotoIds(List.of("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"))
                .userId(member.getUserId().toString())
                .placeId(place.getPlaceId().toString())
                .build();
    }

    @AfterEach
    public void deleteAll(){
        pointRepository.deleteAll();
        attachedPhotoRepository.deleteAll();
        reviewRepository.deleteAll();
        placeRepository.deleteAll();
        memberRepository.deleteAll();
    }

    /**
     * 테스트용 회원
     * @return
     */
    protected Member createMember(){
        UUID userId = UUID.randomUUID();
        Member member = Member.createMember(userId);
        return memberRepository.save(member);
    }

    /**
     * 테스트용 장소
     * @return
     */
    protected Place createPlace(Member member){
        Place place = Place.createPlace(member);
        return placeRepository.save(place);
    }

    /**
     * 테스트용 리뷰
     * @param member
     * @param place
     * @return
     */
    protected Review createReview(Member member, Place place){
        String type = "REVIEW";
        String content = "좋아요";
        return Review.createReview(type, ReviewActionCode.ADD, content, member.getUserId(), place);
    }
}
