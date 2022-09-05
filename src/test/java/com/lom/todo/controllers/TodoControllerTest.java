package com.lom.todo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.lom.todo.dtos.TodoDTO;
import com.lom.todo.dtos.TodoRequestDTO;
import com.lom.todo.dtos.TodoUpdateDTO;
import com.lom.todo.services.TodoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TodoController.class)
class TodoControllerTest {
    @MockBean
    private TodoService todoService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getById_return_Ok() throws Exception {
        when(todoService.getById(1L))
                .thenReturn(getTodoDTO());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/todos/{todoId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("todo 1"));
    }

    @Test
    void getList_return_Ok() throws Exception {
        when(todoService.getList())
                .thenReturn(getListTodoDTO());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("todo 1"));
    }

    @Test
    void create_return_Ok() throws Exception {
        var request = new TodoRequestDTO();
        request.setDescription("todo 1");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(request);

        doNothing().when(todoService).create(any());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void update_return_Ok() throws Exception {
        var request = new TodoUpdateDTO();
        request.setId(1L);
        request.setDescription("todo 1");
        request.setFinished(false);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(request);

        doNothing().when(todoService).update(any());

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void delete_return_Ok() throws Exception {
        doNothing().when(todoService).delete(any());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/todos/{todoId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    private TodoDTO getTodoDTO() {
        var t1 = new TodoDTO();
        t1.setId(1);
        t1.setDescription("todo 1");
        t1.setFinished(false);
        t1.setCreated(new Timestamp(System.currentTimeMillis()));

        return t1;
    }

    private List<TodoDTO> getListTodoDTO() {
        var t1 = new TodoDTO();
        t1.setId(1);
        t1.setDescription("todo 1");
        t1.setFinished(false);
        t1.setCreated(new Timestamp(System.currentTimeMillis()));

        var t2 = new TodoDTO();
        t2.setId(1);
        t2.setDescription("todo 2");
        t2.setFinished(false);
        t2.setCreated(new Timestamp(System.currentTimeMillis()));

        var t3 = new TodoDTO();
        t3.setId(3);
        t3.setDescription("todo 3");
        t3.setFinished(false);
        t3.setCreated(new Timestamp(System.currentTimeMillis()));

        return Arrays.asList(t1, t2, t3);
    }

}