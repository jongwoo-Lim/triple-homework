package com.triple.triplehomework.entity.review;

import com.fasterxml.uuid.Generators;
import com.triple.triplehomework.common.code.ReviewActionCode;
import com.triple.triplehomework.entity.BaseEntity;
import com.triple.triplehomework.entity.place.Place;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "review")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString(exclude = "place")
public class Review extends BaseEntity {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID reviewId;

    private String type;

    @Enumerated(EnumType.STRING)
    private ReviewActionCode action;

    private String content;
    @Column(columnDefinition = "BINARY(16)")
    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pno")
    private Place place;

    @PrePersist
    public void createReviewId() {
        //sequential uuid
        UUID uuid = Generators.timeBasedGenerator().generate();
        String[] uuidArr = uuid.toString().split("-");
        String uuidStr = uuidArr[2]+uuidArr[1]+uuidArr[0]+uuidArr[3]+uuidArr[4];
        StringBuffer sb = new StringBuffer(uuidStr);
        sb.insert(8, "-");
        sb.insert(13, "-");
        sb.insert(18, "-");
        sb.insert(23, "-");
        uuid = UUID.fromString(sb.toString());
        this.reviewId = uuid;
    }

    public static Review createReview(String type, ReviewActionCode action, String content, UUID userId, Place place){
        return Review.builder()
                .type(type)
                .action(action)
                .content(content)
                .userId(userId)
                .place(place)
                .build();
    }

    @Builder
    public Review(String type, ReviewActionCode action, String content, UUID userId, Place place) {
        this.type = type;
        this.action = action;
        this.content = content;
        this.userId = userId;
        this.place = place;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Review review = (Review) o;
        return reviewId != null && Objects.equals(reviewId, review.reviewId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
