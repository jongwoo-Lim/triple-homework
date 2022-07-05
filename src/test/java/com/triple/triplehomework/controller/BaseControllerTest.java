package com.triple.triplehomework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.triple.triplehomework.common.code.ReviewActionCode;
import com.triple.triplehomework.dto.ReviewRequestDto;
import com.triple.triplehomework.entity.member.Member;
import com.triple.triplehomework.entity.place.Place;
import com.triple.triplehomework.entity.review.AttachedPhoto;
import com.triple.triplehomework.entity.review.AttachedPhotoId;
import com.triple.triplehomework.entity.review.Review;
import com.triple.triplehomework.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Disabled
public class BaseControllerTest {

    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected PlaceRepository placeRepository;

    @Autowired
    protected ReviewRepository reviewRepository;

    @Autowired
    protected PointRepository pointRepository;

    @Autowired
    protected AttachedPhotoRepository photoRepository;

    @Autowired
    protected TotalPointRepository totalPointRepository;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected  Member member;
    protected  Place place;

    @BeforeAll
    public void setUp(){
        Member admin = createMember();
        place = createPlace(admin);
        member = createMember();
    }

    @AfterEach
    public  void deleteAll(){
        totalPointRepository.deleteAll();
        pointRepository.deleteAll();
        photoRepository.deleteAll();
        reviewRepository.deleteAll();
    }

    protected ReviewRequestDto createReviewRequestDto(String reviewId, Place place, String content,
                                                      List<String> photoIds, ReviewActionCode actionCode, String removePhotoYn) {
        String placeId = place.getPlaceId() != null ? place.getPlaceId().toString() : "";
        ReviewRequestDto requestDto = ReviewRequestDto.builder()
                .type("REVIEW")
                .action(actionCode)
                .reviewId(reviewId)
                .content(content)
                .attachedPhotoIds(photoIds)
                .userId(member.getUserId().toString())
                .placeId(placeId)
                .removePhotoYn(removePhotoYn)
                .build();
        return requestDto;
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
        String content = "좋아요";
        Review review = Review.createReview(content, member.getUserId(), place);
        return reviewRepository.save(review);
    }

    /**
     * 테스트용 첨부파일
     * @param attachedPhotoId
     * @param photoIds
     * @return
     */
    protected void createAttachedPhoto(AttachedPhotoId attachedPhotoId, List<String> photoIds){
        List<AttachedPhoto> photos = new ArrayList<>();
        for(String strPhotoId : photoIds){
            UUID photoId = UUID.fromString(strPhotoId);
            AttachedPhoto attachedPhoto = AttachedPhoto.createAttachedPhoto(attachedPhotoId, photoId);
            photos.add(attachedPhoto);
        }

        photoRepository.saveAll(photos);
    }
}
