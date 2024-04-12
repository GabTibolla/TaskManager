package com.tde_pwm.aula.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "tasks")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TasksModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 20, nullable = false)
    private String title;

    @Column(length = 150)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(length = 15, nullable = false)
    private Status status;

    @ManyToOne(targetEntity = UsersModel.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "assigned_by_user_id")
    private UsersModel assignedByUser;

    @ManyToOne(targetEntity = UsersModel.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "created_by_user_id")
    private UsersModel createdByUser;

    @CreationTimestamp
    @Column(length = 20)
    private LocalDateTime createdAt;

    // Getters

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status.name();
    }

    public UsersModel getCreatedByUser() {
        return createdByUser;
    }

    public UsersModel getAssignedByUser() {
        return assignedByUser;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Setters

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(String status) {
        this.status = Status.valueOf(status);
    }

    public void setCreatedByUser(UsersModel createdByUser) {
        this.createdByUser = createdByUser;
    }

    public void setAssignedByUser(UsersModel assignedByUser) {
        this.assignedByUser = assignedByUser;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Estrutura de dados aceitos pelo Status
    private enum Status {
        PENDENTE,
        EM_PROGRESSO,
        CONCLUIDO
    }
}
