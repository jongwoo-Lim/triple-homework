package com.triple.triplehomework.entity.review;

import com.triple.triplehomework.common.code.ReviewActionCode;
import com.triple.triplehomework.entity.BaseEntity;
import com.triple.triplehomework.entity.place.Place;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "review")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode(of = "rno", callSuper = true)
@ToString(exclude = "place")
public class Review extends BaseEntity {

    @Id @GeneratedValue
    private Long rno;

    private UUID reviewId;
    private String type;

    @Enumerated(EnumType.STRING)
    private ReviewActionCode action;

    private String content;
    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pno")
    private Place place;

    public static Review createReview(UUID reviewId, String type, ReviewActionCode action, String content, UUID userId, Place place){
        return Review.builder()
                .reviewId(reviewId)
                .type(type)
                .action(action)
                .content(content)
                .userId(userId)
                .place(place)
                .build();
    }
}
