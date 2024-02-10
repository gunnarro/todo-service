package org.gunnarro.microservice.todoservice.repository.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntity {
    @Id
    @Tsid
    @Column(name = "ID")
    private Long id;

    @CreationTimestamp(source = SourceType.DB)
    @Column(name = "CREATED_DATE", nullable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp(source = SourceType.DB)
    @Column(name = "LAST_MODIFIED_DATE", nullable = false)
    private LocalDateTime lastModifiedDate;

    @Column(name = "CREATED_BY_USER", nullable = false)
    private String createdByUser;

    @Column(name = "LAST_MODIFIED_BY_USER", nullable = false)
    private String lastModifiedByUser;
}
