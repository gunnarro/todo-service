package org.gunnarro.microservice.todoservice.repository.entity;

import jakarta.persistence.Column;

public class UserAccount extends BaseEntity {
    @Column(name = "USER_NAME", nullable = false)
    private String userName;
    @Column(name = "STATUS", nullable = false)
    private String status;
}
