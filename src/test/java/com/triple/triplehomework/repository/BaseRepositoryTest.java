package com.triple.triplehomework.repository;

import com.triple.triplehomework.entity.member.Member;
import com.triple.triplehomework.entity.place.Place;
import com.triple.triplehomework.entity.review.Review;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

@DataJpaTest
@Disabled
@ActiveProfiles("test")
public class BaseRepositoryTest {

    protected static final String NOT_REMOVED = "N";

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
        return Review.createReview(content, member.getUserId(), place);
    }
}
