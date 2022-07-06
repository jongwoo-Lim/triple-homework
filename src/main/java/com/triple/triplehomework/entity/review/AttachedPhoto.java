package com.triple.triplehomework.entity.review;

import com.triple.triplehomework.entity.BaseEntity;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

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
@DynamicInsert
@DynamicUpdate
@ToString
public class AttachedPhoto extends BaseEntity {

    @EmbeddedId
    private AttachedPhotoId attachedPhotoId;

    @Column(columnDefinition = "BINARY(16)")
    private UUID photoId;

    // 삭제 여부
    @Column(name = "remove_yn")
    @ColumnDefault("'N'")
    private String removeYn = "N";

    public static AttachedPhoto createAttachedPhoto(AttachedPhotoId attachedPhotoId, UUID photoId){
        attachedPhotoId.increasePhotoNo();

        AttachedPhotoId build = AttachedPhotoId.createAttachedPhotoId(attachedPhotoId.getReview(), attachedPhotoId.getPhotoNo());

        return AttachedPhoto.builder()
                .attachedPhotoId(build)
                .photoId(photoId)
                .removeYn("N")
                .build();
    }

    public void delete(){
        this.removeYn = "Y";
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
