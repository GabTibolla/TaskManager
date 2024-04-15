package com.tde_pwm.aula.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity(name = "subtasks")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class SubTaskModel {

    // Columns

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 150)
    private String description;

    @Column(nullable = false)
    private Integer timeHours;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(targetEntity = TasksModel.class, fetch = FetchType.EAGER)
    @JoinColumn(name="task_id", referencedColumnName = "id")
    private TasksModel task;

    @ManyToOne(targetEntity = UsersModel.class, fetch = FetchType.EAGER)
    @JoinColumn(name="assigned_by", referencedColumnName = "id")
    private UsersModel assignedBy;


    // Getters

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Integer getTimeHours() {
        return timeHours;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public TasksModel getTask() {
        return task;
    }

    public UsersModel getAssignedBy() {
        return assignedBy;
    }

    // Setters

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTimeHours(Integer timeHours) {
        this.timeHours = timeHours;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setTask(TasksModel task) {
        this.task = task;
    }

    public void setAssignedBy(UsersModel assignedBy) {
        this.assignedBy = assignedBy;
    }
}
