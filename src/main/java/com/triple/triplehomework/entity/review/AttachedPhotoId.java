package com.triple.triplehomework.entity.review;

import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString(exclude = "review")
public class AttachedPhotoId implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rno")
    private Review review;
    private Long photoNo;

    public static AttachedPhotoId createAttachedPhotoId(Review review, Long photoNo){
        return AttachedPhotoId.builder()
                .review(review)
                .photoNo(photoNo)
                .build();
    }
}
