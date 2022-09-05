package com.lom.todo.services;

import com.lom.todo.dtos.TodoDTO;
import com.lom.todo.dtos.TodoRequestDTO;
import com.lom.todo.dtos.TodoUpdateDTO;
import com.lom.todo.entity.Todo;
import com.lom.todo.ex.TodoNotFoundException;
import com.lom.todo.repository.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TodoService {
    private final TodoRepository todoRepository;

    @Autowired
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public TodoDTO getById(Long id) {
        var todoEntity = todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException(id));

        var todoDTO = new TodoDTO();
        todoDTO.setId(todoEntity.getId());
        todoDTO.setDescription(todoEntity.getDescription());
        todoDTO.setFinished(todoEntity.getFinished());
        todoDTO.setCreated(todoEntity.getCreated());

        return todoDTO;
    }

    public List<TodoDTO> getList() {
        return todoRepository.findAllByFinishedFalse()
                .stream()
                .map(x -> {
                    var todoDTO = new TodoDTO();
                    todoDTO.setId(x.getId());
                    todoDTO.setDescription(x.getDescription());
                    todoDTO.setFinished(x.getFinished());
                    todoDTO.setCreated(x.getCreated());

                    return todoDTO;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void create(TodoRequestDTO request) {
        var todoEntity = new Todo();
        todoEntity.setDescription(request.getDescription());

        todoRepository.save(todoEntity);
    }

    @Transactional
    public void update(TodoUpdateDTO request) {
        var todoEntity = todoRepository.findById(request.getId())
                .orElseThrow(() -> new TodoNotFoundException(request.getId()));
        todoEntity.setDescription(request.getDescription());
        todoEntity.setFinished(request.getFinished());

        todoRepository.save(todoEntity);
        todoRepository.flush();
    }

    @Transactional
    public void delete(Long id) {
        var todoEntity = todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException(id));

        todoRepository.delete(todoEntity);
        todoRepository.flush();
    }
}
