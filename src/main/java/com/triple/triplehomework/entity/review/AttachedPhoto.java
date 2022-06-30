package com.triple.triplehomework.entity.review;

import com.triple.triplehomework.entity.BaseEntity;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "REVIEW_ATTACHED_PHOTO")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@ToString
public class AttachedPhoto extends BaseEntity {

    @EmbeddedId
    private AttachedPhotoId attachedPhotoId;

    @Column(columnDefinition = "BINARY(16)")
    private UUID photoId;

    public static AttachedPhoto createAttachedPhoto(AttachedPhotoId attachedPhotoId, UUID photoId){
        attachedPhotoId.increasePhotoNo();

        AttachedPhotoId build = AttachedPhotoId.createAttachedPhotoId(attachedPhotoId.getReview(), attachedPhotoId.getPhotoNo());

        return AttachedPhoto.builder()
                .attachedPhotoId(build)
                .photoId(photoId)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AttachedPhoto that = (AttachedPhoto) o;
        return attachedPhotoId != null && Objects.equals(attachedPhotoId, that.attachedPhotoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attachedPhotoId);
    }
}
