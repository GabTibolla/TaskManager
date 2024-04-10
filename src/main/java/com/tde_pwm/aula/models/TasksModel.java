package com.tde_pwm.aula.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "tasks")
public class TasksModel {
    @Id
    private String id = UUID.randomUUID().toString();

    @Column(length = 20, nullable = false)
    private String title;

    @Column(length = 150)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(length = 15, nullable = false)
    private Status status;

    @ManyToOne
    private UsersModel createdByUser;

    @ManyToOne
    private UsersModel assignedByUser;

    @Column(length = 20)
    private LocalDateTime createdAt;

    // Getters

    public String getId() {
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

//    public boolean isValidStatus(String status) {
//        for (Status s : Status.values()) {
//            if (s.name().equalsIgnoreCase(status)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public void setStatus(String status) {
//        if (isValidStatus(status)) {
//            this.status = Status.valueOf(status.toUpperCase());
//        } else {
//            throw new IllegalArgumentException("Status inválido: " + status);
//        }
//    }
}
