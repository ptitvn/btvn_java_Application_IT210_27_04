package com.example.todo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{error.content.empty}")
    private String content;

    @FutureOrPresent(message = "{error.dueDate.futureOrPresent}")
    @NotNull(message = "{error.dueDate.empty}")
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "{error.status.empty}")
    private Status status;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "{error.priority.empty}")
    private Priority priority;

    // Enum cho trạng thái
    public enum Status {
        NOT_STARTED,
        IN_PROGRESS,
        COMPLETED
    }

    // Enum cho độ ưu tiên
    public enum Priority {
        LOW,
        MEDIUM,
        HIGH
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }
}
