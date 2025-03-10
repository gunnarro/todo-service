package org.gunnarro.microservice.todoservice.service.impl;


import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.itextpdf.html2pdf.HtmlConverter;
import lombok.extern.slf4j.Slf4j;
import org.gunnarro.microservice.todoservice.domain.dto.todo.*;
import org.gunnarro.microservice.todoservice.domain.mapper.TodoMapper;
import org.gunnarro.microservice.todoservice.exception.ApplicationException;
import org.gunnarro.microservice.todoservice.exception.RestInputValidationException;
import org.gunnarro.microservice.todoservice.repository.*;
import org.gunnarro.microservice.todoservice.repository.entity.Todo;
import org.gunnarro.microservice.todoservice.repository.entity.TodoItem;
import org.gunnarro.microservice.todoservice.repository.entity.TodoItemImage;
import org.gunnarro.microservice.todoservice.service.TodoService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.util.*;

@Slf4j
@Service
public class TodoServiceImpl implements TodoService {

    private final static String DEFAULT_USER = "guest";

    private final TodoRepository todoRepository;
    private final TodoItemRepository todoItemRepository;
    private final ParticipantRepository participantRepository;
    private final ApprovalRepository approvalRepository;
    private final ImageDataRepository imageDataRepository;

    public TodoServiceImpl(TodoRepository todoRepository
            , TodoItemRepository todoItemRepository
            , ParticipantRepository participantRepository
            , ApprovalRepository approvalRepository
            , ImageDataRepository imageDataRepository) {
        this.todoRepository = todoRepository;
        this.todoItemRepository = todoItemRepository;
        this.participantRepository = participantRepository;
        this.approvalRepository = approvalRepository;
        this.imageDataRepository = imageDataRepository;
    }

    //-------------------------------------------------------------------
    // Todo
    //-------------------------------------------------------------------

    /**
     * We don't store the HTML on disk; the method below creates the HTML file in memory.
     */
    @Override
    public byte[] getTodoAsPdf(Long todoId) {
        TodoDto todo = getTodo(todoId);
        try {
            File resource = new ClassPathResource("todo-template.mustache").getFile();
            String todoTemplate = new String(Files.readAllBytes(resource.toPath()));
            String html = createHtml(todo, todoTemplate);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            HtmlConverter.convertToPdf(html, outputStream);
            outputStream.close();
            return outputStream.toByteArray();
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }

    @Override
    public List<TodoDto> getTodosByUserName(String userName) {
        return TodoMapper.toTodoDtoList(todoRepository.getTodosByUserName(userName));
    }

    @Override
    public TodoDto getTodo(Long todoId) {
        log.debug("todoId={}", todoId);
        Optional<Todo> todoOpt = todoRepository.getTodoById(todoId);
        if (todoOpt.isPresent())
            return TodoMapper.toTodoDto(todoOpt.get());
        else {
            return new TodoDto();
            //throw new NotFoundException("todoId=" + todoId);
        }
    }

    // @Mapper(componentModel = "spring") may also use mapstruct
    @Transactional
    @Override
    public TodoDto createTodo(TodoDto todoDto) {
        try {
            log.debug("save todo, {}", todoDto);
            return TodoMapper.toTodoDto(todoRepository.save(TodoMapper.fromTodoDto(todoDto)));
        } catch (DataIntegrityViolationException ex) {
            throw new RestInputValidationException(ex.getMessage());
        } catch (Exception e) {
            throw new ApplicationException(String.format("error add todo! error: %s", e.getMessage()), e.getCause());
        }
    }


    @Transactional
    @Override
    public TodoDto updateTodo(TodoDto todoDto) {
        try {
            return TodoMapper.toTodoDto(TodoMapper.fromTodoDto(todoDto));
        } catch (DataIntegrityViolationException ex) {
            throw new RestInputValidationException(ex.getMessage());
        } catch (Exception e) {
            throw new ApplicationException(String.format("error update todo! error: %s", e.getMessage()), e.getCause());
        }
    }

    @Override
    public void deleteTodo(Long todoId) {
        todoRepository.deleteById(todoId);
        log.debug("deleted todo, todoId={}", todoId);
    }

    //-------------------------------------------------------------------
    // Todo items
    //-------------------------------------------------------------------

    public TodoItemDto getTodoItem(Long todoId, Long todoItemId) {
        return TodoMapper.toTodoItemDto(todoItemRepository.getTodoItem(todoId, todoItemId).orElse(null));
    }

    @Transactional
    @Override
    public TodoItemDto createTodoItem(TodoItemDto todoItemDto) {
        try {
            log.debug("{}", todoItemDto);
            TodoItem todoItem = TodoMapper.fromTodoItemDto(todoItemDto);
            log.debug("{}", todoItem);
            return TodoMapper.toTodoItemDto(todoItemRepository.save(todoItem));
        } catch (DataIntegrityViolationException ex) {
            throw new RestInputValidationException(ex.getMessage());
        } catch (Exception e) {
            throw new ApplicationException(String.format("error add todo item! error: %s", e.getMessage()), e.getCause());
        }
    }

    @Transactional
    @Override
    public TodoItemDto updateTodoItem(TodoItemDto todoItemDto) {
        TodoItem todoItem = TodoMapper.fromTodoItemDto(todoItemDto);
        log.debug("{}", todoItem);
        return TodoMapper.toTodoItemDto(todoItemRepository.save(todoItem));
    }

    @Override
    public void deleteTodoItem(Long todoId, Long todoItemId) {
        todoItemRepository.deleteById(todoItemId);
    }


    public boolean isApprovedByAll(Long todoItemId) {
        return false;
    }

    //-------------------------------------------------------------------
    // Participants
    //-------------------------------------------------------------------
    @Override
    public List<ParticipantDto> getParticipants(Long todoId) {
        return TodoMapper.toParticipantDtoList(participantRepository.getParticipants(todoId));
    }

    @Override
    public ParticipantDto createParticipant(ParticipantDto participantDto) {
        return TodoMapper.toParticipantDto(participantRepository.save(TodoMapper.fromParticipantDto(participantDto)));
    }

    @Override
    public ParticipantDto updateParticipant(ParticipantDto participantDto) {
        return TodoMapper.toParticipantDto(participantRepository.save(TodoMapper.fromParticipantDto(participantDto)));
    }

    @Override
    public void deleteParticipant(Long todoId, Long participantId) {
        participantRepository.deleteById(participantId);
        log.debug("deleted participant, todoId={}, participantId={}", todoId, participantId);
    }

    //-------------------------------------------------------------------
    // Approvals
    //-------------------------------------------------------------------
    @Override
    public List<ApprovalDto> getApprovals(Long todoId, Long todoItemId) {
        return TodoMapper.toApprovalDtoList(approvalRepository.getApprovals(todoItemId));
    }

    @Override
    public ApprovalDto createApproval(ApprovalDto approvalDto) {
        return TodoMapper.toApprovalDto(approvalRepository.save(TodoMapper.fromApprovalDto(approvalDto)));
    }

    @Override
    public ApprovalDto updateApproval(ApprovalDto approvalDto) {
        return TodoMapper.toApprovalDto(approvalRepository.save(TodoMapper.fromApprovalDto(approvalDto)));
    }

    @Override
    public void deleteApproval(Long todoItemId, Long participantId) {
        approvalRepository.deleteApproval(todoItemId, participantId);
        log.debug("deleted approval, todoItemId={}, participantId={}", todoItemId, participantId);
    }

    //-------------------------------------------------------------------
    // History
    //-------------------------------------------------------------------
    public List<TodoHistoryDto> getTodoHistory(Long todoId) {
        List<TodoHistoryDto> todoHistoryDtoList = new ArrayList<>();
        Revisions<Long, Todo> revisions = this.todoRepository.findRevisions(todoId);
        Iterator<Revision<Long, Todo>> revisionsIterator = revisions.stream().iterator();
        while (revisionsIterator.hasNext()) {
            Revision<Long, Todo> rev = revisionsIterator.next();
            todoHistoryDtoList.add(TodoMapper.toTodoHistoryDto(rev.getEntity(), null, rev.getMetadata().getRevisionType().name()));
        }
        return todoHistoryDtoList;
    }

    public List<TodoItemHistoryDto> getTodoItemHistory(Long todoId, Long todoItemId) {
        List<TodoItemHistoryDto> todoItemHistoryDtoList = new ArrayList<>();
        Revisions<Long, TodoItem> revisions = this.todoItemRepository.findRevisions(todoItemId);
        Iterator<Revision<Long, TodoItem>> revisionsIterator = revisions.stream().iterator();
        while (revisionsIterator.hasNext()) {
            Revision<Long, TodoItem> rev = revisionsIterator.next();
            todoItemHistoryDtoList.add(TodoMapper.toTodoItemHistoryDto(rev.getEntity(), null, rev.getMetadata().getRevisionType().name()));
        }
        return todoItemHistoryDtoList;
    }

    private String createHtml(TodoDto todoDto, String todoMustacheTemplate) {
        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile(new StringReader(todoMustacheTemplate), "");
        Map<String, Object> context = new HashMap<>();
        context.put("todo", todoDto);
        StringWriter writer = new StringWriter();
        mustache.execute(writer, context);
        return writer.toString();
    }

    public TodoItemImage getImage(String name) {
         //imageDataRepository.findByName(name);

        return new TodoItemImage();
    }


    public TodoItemImage createImage(TodoItemImage todoItemImage) {
        return null;
    }

    public TodoItemImage updateImage(TodoItemImage todoItemImage) {
        return null;
    }

    public void deleteImage(Long todoItemId, Long imageId) {

    }
}