package com.triple.triplehomework.controller;

import com.triple.triplehomework.common.code.ReviewActionCode;
import com.triple.triplehomework.dto.ReviewRequestDto;
import com.triple.triplehomework.entity.place.Place;
import com.triple.triplehomework.entity.review.AttachedPhotoId;
import com.triple.triplehomework.entity.review.Review;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReviewControllerTest extends BaseControllerTest{


    @Test
    @DisplayName("리뷰 삭제 이벤트 테스트")
    public void deleteReviewEvent() throws Exception{
        // Given
        List<String> photoIds = List.of("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332");
        Review savedReview = createReview(member, place);
        AttachedPhotoId attachedPhotoId = AttachedPhotoId.createAttachedPhotoId(savedReview, 0L);
        createAttachedPhoto(attachedPhotoId, photoIds);

        ReviewRequestDto requestDto = createReviewRequestDto(savedReview.getReviewId().toString(), place, "", photoIds, ReviewActionCode.DELETE, "N");

        // When
        mockMvc.perform(post("/events")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").exists())
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("data").value(true))
        ;

        // Then
        int existingPhoto = photoRepository.findByReview(savedReview, "N").size();
        assertThat(existingPhoto).isEqualTo(0);

        Optional<Review> optionalReview = reviewRepository.findById(savedReview.getReviewId());
        assertThat(optionalReview.get().getRemoveYn()).isEqualTo("Y");
    }


    @Test
    @DisplayName("리뷰 첨부 파일 삭제 이벤트 테스트")
    public void deleteReviewEvent_Photo_Remove() throws Exception{
        // Given
        List<String> photoIds = List.of("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332");
        Review savedReview = createReview(member, place);
        AttachedPhotoId attachedPhotoId = AttachedPhotoId.createAttachedPhotoId(savedReview, 0L);
        createAttachedPhoto(attachedPhotoId, photoIds);

        ReviewRequestDto requestDto = createReviewRequestDto(savedReview.getReviewId().toString(), place, "", photoIds, ReviewActionCode.DELETE, "Y");

        // When
        mockMvc.perform(post("/events")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").exists())
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("data").value(true))
        ;

        // Then
        int existingPhoto = photoRepository.findByReview(savedReview, "N").size();
        assertThat(existingPhoto).isEqualTo(0);
    }

    @Test
    @DisplayName("리뷰 수정시 입력 값이 잘못된 경우에 에러가 발생하는 테스트")
    public void modReviewEvent_Bad_Request_Wrong_Input() throws Exception{
        // Given
        Review savedReview = createReview(member, place);
        String updateContent = "review update content..";
        // 리뷰 ID 누락
        List<String> photoIds = Collections.emptyList();
        ReviewRequestDto requestDto = createReviewRequestDto("", place, updateContent, photoIds, ReviewActionCode.MOD, "");

        // When && Then
        mockMvc.perform(post("/events")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").exists())
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("message").value("리뷰 ID는 필수값입니다."))
        ;
    }

    @Test
    @DisplayName("리뷰 수정 이벤트 테스트")
    public void modReviewEvent() throws Exception{
        // Given
        Review savedReview = createReview(member, place);

        String updateContent = "review update content..";
        List<String> photoIds = Collections.emptyList();
        ReviewRequestDto requestDto = createReviewRequestDto(savedReview.getReviewId().toString(), place, updateContent, null, ReviewActionCode.MOD, "");

        // When && Then
        mockMvc.perform(post("/events")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").exists())
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("data").exists())
                .andExpect(jsonPath("data.reviewId").exists())
                .andExpect(jsonPath("data.content").value(updateContent))
                .andExpect(jsonPath("data.attachedPhotoIds").isArray())
                .andExpect(jsonPath("data.removeYn").value("N"))
        ;
    }

    @Test
    @DisplayName("한 회원이 한 장소의 리뷰 두 번 작성할 때 에러 발생하는 테스트")
    public void addReviewEvent_Bad_Request_Existed_Review() throws Exception{
        // Given
        Review review = createReview(member, place);

        String content = "review add content..";
        List<String> photoIds = List.of("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332");
        ReviewRequestDto requestDto = createReviewRequestDto("", place, content, photoIds, ReviewActionCode.ADD, "");

        // When && Then
        mockMvc.perform(post("/events")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").exists())
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("message").value("한 장소에 리뷰는 한 번만 작성하실 수 있습니다."))
        ;
    }

    @Test
    @DisplayName("리뷰 생성시 리뷰 내용이 비어있는 에러가 발생하는 테스트")
    public void addReviewEvent_Bad_Request_Wrong_Content() throws Exception{
        // Given
        // 리뷰 내용 누락
        List<String> photoIds = List.of("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332");
        ReviewRequestDto requestDto = createReviewRequestDto("", place, "", photoIds, ReviewActionCode.ADD, "");

        // When && Then
        mockMvc.perform(post("/events")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").exists())
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("message").value("리뷰 내용은 비어있을 수 없습니다."))
        ;
    }

    @Test
    @DisplayName("리뷰 생성시 입력 값이 잘못된 경우에 에러가 발생하는 테스트")
    public void addReviewEvent_Bad_Request_Wrong_Input() throws Exception{
        // Given
        // 해당 장소 ID 누락
        Place place = Place.createPlace(member);
        List<String> photoIds = List.of("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332");
        ReviewRequestDto requestDto = createReviewRequestDto("", place, "", photoIds, ReviewActionCode.ADD, "");

        // When && Then
        mockMvc.perform(post("/events")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").exists())
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("errors[0].field").exists())
                .andExpect(jsonPath("errors[0].objectName").exists())
                .andExpect(jsonPath("errors[0].defaultMessage").exists())
        ;
    }

    @Test
    @DisplayName("리뷰 생성 이벤트")
    public void addReviewEvent() throws Exception{
        // Given
        String content = "review add content..";
        List<String> photoIds = List.of("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332");
        ReviewRequestDto requestDto = createReviewRequestDto("", place, content, photoIds, ReviewActionCode.ADD, "");

        // When && Then
        mockMvc.perform(post("/events")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").exists())
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("data").exists())
                .andExpect(jsonPath("data.reviewId").exists())
                .andExpect(jsonPath("data.content").value(content))
                .andExpect(jsonPath("data.attachedPhotoIds").isArray())
                .andExpect(jsonPath("data.removeYn").value("N"))
                ;
    }

}