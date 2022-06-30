package com.triple.triplehomework.entity.review;

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
@ToString(exclude = "place")
public class Review extends BaseEntity {

    @Id @GeneratedValue
    private Long rno;

    private UUID reviewId;
    private String type;
    private String action;
    private String content;
    private Long mno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pno")
    private Place place;

    public static Review createReview(UUID reviewId, String type, String action, String content, Long mno, Place place){
        return Review.builder()
                .reviewId(reviewId)
                .type(type)
                .action(action)
                .content(content)
                .mno(mno)
                .place(place)
                .build();
    }
}
