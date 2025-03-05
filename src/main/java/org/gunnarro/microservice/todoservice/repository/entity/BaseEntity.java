package org.gunnarro.microservice.todoservice.repository.entity;

import io.hypersistence.tsid.TSID;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


@Getter
@Setter
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @NotAudited
    @Id
    //@Tsid
    @Column(name = "ID", updatable = false)
    private Long id;

    @CreationTimestamp(source = SourceType.DB)
    @Column(name = "CREATED_DATE", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp(source = SourceType.DB)
    @Column(name = "LAST_MODIFIED_DATE", nullable = false)
    private LocalDateTime lastModifiedDate;

    @CreatedBy
    @Column(name = "CREATED_BY_USER", nullable = false, updatable = false)
    private String createdByUser;

    @LastModifiedBy
    @Audited(withModifiedFlag = true)
    @Column(name = "LAST_MODIFIED_BY_USER", nullable = false)
    private String lastModifiedByUser;
}
