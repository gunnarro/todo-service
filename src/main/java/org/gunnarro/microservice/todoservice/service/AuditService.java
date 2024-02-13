package org.gunnarro.microservice.todoservice.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.gunnarro.microservice.todoservice.repository.entity.Todo;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.hibernate.envers.strategy.ValidityAuditStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class AuditService {

    @PersistenceContext
    EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<Todo> getTodoHistory(Long todoId) {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        AuditQuery auditQuery = auditReader.createQuery().forRevisionsOfEntity(Todo.class, true, true);
        auditQuery.add(AuditEntity.id().eq(todoId));
        List<Todo> history = auditQuery.getResultList();
        log.debug("{}", history);
        return history;
    }

}
