package org.gunnarro.microservice.todoservice.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Table(name = "TODO_ITEM_IMAGE", schema = "todo")
@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TodoItemImage extends BaseEntity {

    @Column(name = "TODO_ITEM_ID", nullable = false)
    private Long todoItemId;
    @Column(name = "FILENAME", nullable = false)
    private String fileName;
    @Column(name = "FILE_SIZE", nullable = false)
    private Long fileSize;
    @Lob
    @Column(name = "IMAGE", nullable = false, length = 10000)
    private byte[] image;
}
