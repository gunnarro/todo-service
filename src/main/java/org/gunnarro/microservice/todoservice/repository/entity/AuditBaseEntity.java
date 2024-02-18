package org.gunnarro.microservice.todoservice.repository.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


/**
 * Only used for read, therefore immutable.
 */
@Immutable
@Getter
@SuperBuilder
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
public class AuditBaseEntity {


    @ToString.Include(rank = 1)
    @Id
    @Column(name = "ID", nullable = false, insertable = false, updatable = false)
    private Long id;

    @CreationTimestamp(source = SourceType.DB)
    @Column(name = "CREATED_DATE", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp(source = SourceType.DB)
    @Column(name = "LAST_MODIFIED_DATE", nullable = false, insertable = false, updatable = false)
    private LocalDateTime lastModifiedDate;

    @Column(name = "CREATED_BY_USER", nullable = false, insertable = false, updatable = false)
    private String createdByUser;

    @Column(name = "LAST_MODIFIED_BY_USER", nullable = false, insertable = false, updatable = false)
    private String lastModifiedByUser;

    @Column(name = "REVISION_ID", nullable = false, insertable = false, updatable = false)
    private Integer revisionId;

    @Column(name = "REVISION_END_ID", nullable = false, insertable = false, updatable = false)
    private Integer revisionEndId;

    @Schema(description = "the revision type, 0: ADD A database table row was inserted. 1: MOD A database table row was updated. 2: DEL A database table row was deleted.")
    @Column(name = "REVISION_TYPE", nullable = false, insertable = false, updatable = false)
    private Integer revisionType;
}
