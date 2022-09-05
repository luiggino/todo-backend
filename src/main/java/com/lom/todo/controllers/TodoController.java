package com.lom.todo.controllers;

import com.lom.todo.dtos.TodoDTO;
import com.lom.todo.dtos.TodoRequestDTO;
import com.lom.todo.dtos.TodoUpdateDTO;
import com.lom.todo.services.TodoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
@Slf4j
public class TodoController {
    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping(path = "/{todoId}")
    public ResponseEntity<TodoDTO> getById(@PathVariable Long todoId) {
        return new ResponseEntity<>(todoService.getById(todoId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<TodoDTO>> getList() {
        return new ResponseEntity<>(todoService.getList(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody TodoRequestDTO request) {
        todoService.create(request);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody TodoUpdateDTO request) {
        todoService.update(request);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "{todoId}")
    public ResponseEntity<Void> delete(@PathVariable Long todoId) {
        todoService.delete(todoId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
