package org.gunnarro.microservice.todoservice.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.gunnarro.microservice.todoservice.domain.dto.todo.TodoHistoryDto;
import org.gunnarro.microservice.todoservice.domain.mapper.TodoMapper;
import org.gunnarro.microservice.todoservice.repository.entity.Todo;
import org.gunnarro.microservice.todoservice.repository.entity.TodoHistory;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class AuditService {
/*
    @PersistenceContext
    EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<TodoHistoryDto> getTodoHistory(Long todoId) {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        AuditQuery auditQuery = auditReader.createQuery()
                .forRevisionsOfEntity(Todo.class, true, true)
                .add(AuditEntity.id().eq(todoId));
               // .addProjection(AuditEntity.property("revisionId"))
               // .addProjection(AuditEntity.property("revisionType"));
        List<Todo> history = auditQuery.getResultList();
        history.forEach(h ->log.debug("{}", h));
        return null;//TodoMapper.toTodoHistoryDtoList(List.of());
    }
*/
}
