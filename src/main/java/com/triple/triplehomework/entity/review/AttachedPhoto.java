package com.triple.triplehomework.entity.review;

import com.triple.triplehomework.entity.BaseEntity;
import lombok.*;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "REVIEW_ATTACHED_PHOTO")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
public class AttachedPhoto extends BaseEntity {

    @EmbeddedId
    private AttachedPhotoId attachedPhotoId;
    private UUID photoId;

    public static AttachedPhoto createAttachedPhoto(AttachedPhotoId attachedPhotoId, UUID photoId){
        return AttachedPhoto.builder()
                .attachedPhotoId(attachedPhotoId)
                .photoId(photoId)
                .build();
    }
}
