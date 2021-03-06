package com.triple.triplehomework.entity;


import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(value = AuditingEntityListener.class)
@Getter
@ToString
public class BaseEntity {

    @CreatedDate
    @Column(name = "reg_dt", updatable = false)
    private LocalDateTime regDate;

    @LastModifiedDate
    @Column(name = "upd_dt")
    private LocalDateTime updDate;
}
