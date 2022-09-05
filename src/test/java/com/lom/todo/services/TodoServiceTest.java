package com.lom.todo.services;

import com.lom.todo.dtos.TodoDTO;
import com.lom.todo.dtos.TodoRequestDTO;
import com.lom.todo.dtos.TodoUpdateDTO;
import com.lom.todo.entity.Todo;
import com.lom.todo.ex.TodoNotFoundException;
import com.lom.todo.repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {
    @Mock
    private TodoRepository todoRepository;
    @InjectMocks
    private TodoService todoService;

    @Test
    void getById_return_Ok() {
        when(todoRepository.findById(any()))
                .thenReturn(Optional.of(getTodo()));
        var response = todoService.getById(1L);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1);
        assertThat(response.getDescription()).isEqualTo("todo 1");
        assertThat(response.getFinished()).isEqualTo(false);
    }

    @Test
    void getById_return_Exception() {
        when(todoRepository.findById(any()))
                .thenThrow(new TodoNotFoundException(1L));

        assertThatExceptionOfType(TodoNotFoundException.class)
                .isThrownBy(() -> todoService.getById(1L))
                .withMessage("Not found '1'")
                .withNoCause();
    }

    @Test
    void getList_return_Ok() {
        when(todoRepository.findAllByFinishedFalse())
                .thenReturn(getListTodo());
        var response = todoService.getList();

        assertThat(response)
                .hasSize(3)
                .extracting(TodoDTO::getDescription)
                .containsExactlyInAnyOrder("todo 1", "todo 2", "todo 3");
    }

    @Test
    void create_return_Ok() {
        var todoRequestDTO = new TodoRequestDTO();
        todoRequestDTO.setDescription("todo 1");

        when(todoRepository.save(any()))
                .thenReturn(getTodo());

        todoService.create(todoRequestDTO);

        verify(todoRepository, times(1))
                .save(any());
    }

    @Test
    void update_return_Ok() {
        var todoUpdateDTO = new TodoUpdateDTO();
        todoUpdateDTO.setId(1L);
        todoUpdateDTO.setDescription("todo 1");
        todoUpdateDTO.setFinished(false);

        when(todoRepository.findById(any()))
                .thenReturn(Optional.of(getTodo()));

        when(todoRepository.save(any()))
                .thenReturn(getTodo());
        doNothing().when(todoRepository).flush();

        todoService.update(todoUpdateDTO);

        verify(todoRepository, times(1))
                .findById(any());
    }

    @Test
    void update_return_exception() {
        var todoUpdateDTO = new TodoUpdateDTO();
        todoUpdateDTO.setId(1L);
        todoUpdateDTO.setDescription("todo 1");
        todoUpdateDTO.setFinished(false);

        when(todoRepository.findById(any()))
                .thenThrow(new TodoNotFoundException(1L));

        assertThatExceptionOfType(TodoNotFoundException.class)
                .isThrownBy(() -> todoService.update(todoUpdateDTO))
                .withMessage("Not found '1'")
                .withNoCause();
    }

    @Test
    void delete_return_Ok() {
        when(todoRepository.findById(any()))
                .thenReturn(Optional.of(getTodo()));

        doNothing().when(todoRepository).delete(any());
        doNothing().when(todoRepository).flush();

        todoService.delete(1L);

        verify(todoRepository, times(1))
                .findById(any());
    }

    @Test
    void delete_return_exception() {
        when(todoRepository.findById(any()))
                .thenThrow(new TodoNotFoundException(1L));

        assertThatExceptionOfType(TodoNotFoundException.class)
                .isThrownBy(() -> todoService.delete(1L))
                .withMessage("Not found '1'")
                .withNoCause();
    }

    private Todo getTodo() {
        var t1 = new Todo();
        t1.setId(1);
        t1.setDescription("todo 1");
        t1.setFinished(false);
        t1.setCreated(new Timestamp(System.currentTimeMillis()));

        return t1;
    }

    private List<Todo> getListTodo() {
        var t1 = new Todo();
        t1.setId(1);
        t1.setDescription("todo 1");
        t1.setFinished(false);
        t1.setCreated(new Timestamp(System.currentTimeMillis()));

        var t2 = new Todo();
        t2.setId(1);
        t2.setDescription("todo 2");
        t2.setFinished(false);
        t2.setCreated(new Timestamp(System.currentTimeMillis()));

        var t3 = new Todo();
        t3.setId(3);
        t3.setDescription("todo 3");
        t3.setFinished(false);
        t3.setCreated(new Timestamp(System.currentTimeMillis()));

        return Arrays.asList(t1, t2, t3);
    }
}