package com.triple.triplehomework.repository;

import com.triple.triplehomework.entity.member.Member;
import com.triple.triplehomework.entity.place.Place;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class PlaceRepositoryTest extends BaseRepositoryTest{

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("장소 등록 테스트")
    public void createPlace_test() throws Exception{
        // Given
        UUID placeId = UUID.randomUUID();
        Member member = createMember();
        Place place = Place.createPlace(placeId, member);
        // When
        Place savedPlace = placeRepository.save(place);
        placeRepository.findAll();

        // Then
        assertThat(savedPlace.getPno()).isNotNull();
        assertThat(savedPlace.getPlaceId()).isEqualTo(placeId);
    }



}