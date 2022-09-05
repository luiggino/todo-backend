package com.lom.todo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TodoUpdateDTO extends TodoRequestDTO {
    private long id;

    private Boolean finished;
}
