package org.gunnarro.microservice.todoservice.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntity {
   // @UuidGenerator
    private UUID uuid;
    @Column(name = "CREATED_DATE", nullable = false)
    private LocalDateTime createdDate;
    @Column(name = "LAST_MODIFIED_DATE", nullable = false)
    private LocalDateTime lastModifiedDate;
    @Column(name = "CREATED_BY_USER", nullable = false)
    private String createdByUser;
    @Column(name = "LAST_MODIFIED_BY_USER", nullable = false)
    private String lastModifiedByUser;
}
