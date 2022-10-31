package com.innople.devpleyground.dpfpapi.domains.base;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {
    /*
        set default tiimezone to UTC for aplication.
        or jvm option -Duser.timezone=UTC
    */
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createDate;

    @Setter
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifyDate;
}
