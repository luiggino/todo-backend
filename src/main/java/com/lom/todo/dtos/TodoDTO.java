package com.lom.todo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TodoDTO extends TodoUpdateDTO {
    private java.sql.Timestamp created;
}
