package com.triple.triplehomework.entity.review;

import com.fasterxml.uuid.Generators;
import com.triple.triplehomework.entity.BaseEntity;
import com.triple.triplehomework.entity.place.Place;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "review")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@DynamicInsert
@ToString(exclude = "place")
public class Review extends BaseEntity {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID reviewId;

    private String content;
    @Column(columnDefinition = "BINARY(16)")
    private UUID userId;

    // 삭제 여부
    @Column(name = "remove_yn")
    @ColumnDefault("'N'")
    private String removeYn = "N";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
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

    public static Review createReview(String content, UUID userId, Place place){
        return Review.builder()
                .content(content)
                .userId(userId)
                .place(place)
                .build();
    }

    /**
     * 리뷰 내용 수정
     * @param content
     */
    public void updateContent(String content){
        if(StringUtils.hasText(content)){
            this.content = content;
        }
    }


    /**
     * 리뷰 삭제
     * 삭제 여부 컬럼 Y
     */
    public void delete() {
        this.removeYn = "Y";
    }

    @Builder
    public Review(String content, UUID userId, Place place) {
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
