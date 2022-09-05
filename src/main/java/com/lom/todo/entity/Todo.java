package com.lom.todo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "todos")
public class Todo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Size(min = 10, message = "Enter at least 10 Characters...")
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "finished", columnDefinition = "boolean default false", nullable = false)
    private Boolean finished = false;

    @Column(name = "created", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false)
    @CreationTimestamp
    private java.sql.Timestamp created;
}
