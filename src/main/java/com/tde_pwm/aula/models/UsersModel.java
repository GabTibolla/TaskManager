package com.tde_pwm.aula.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "users")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")

public class UsersModel {

    // Colunas
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable=false, length=50)
    private String name;

    @Column(nullable = false, length=50)
    private String mail;

    @Column(nullable = false, length = 10)
    private Permission permission;

    @Column(length = 15)
    private String cpf;

    @CreationTimestamp
    @Column(nullable = false, length = 15)
    private LocalDateTime createdAt;

    @Column(length = 15)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "createdByUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TasksModel> tasks = new ArrayList<>();

    @OneToMany(mappedBy = "assignedByUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TasksModel> tasksAssigned = new ArrayList<>();

    @OneToMany(mappedBy = "assignedBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubTaskModel> tasksAssignedSubTask = new ArrayList<>();

// Getters

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMail() {
        return mail;
    }

    public String getPermission() {
        return permission.name();
    }

    public String getCpf() {
        return cpf;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Setters


    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setPermission(String permission) {
        this.permission = Permission.valueOf(permission.toUpperCase());
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {this.updatedAt = updatedAt;}

    public enum Permission {
        ADMIN,
        DEV,
        USER,
    }
}
