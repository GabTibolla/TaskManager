package com.tde_pwm.aula.models;

import com.tde_pwm.aula.helpers.UtilHelper;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "users")
public class UsersModel {

    // Colunas

    @Id
    private String id = UUID.randomUUID().toString();

    @Column(nullable=false, length=50)
    private String name;

    @Column(nullable = false, length = 40)
    private String password;

    @Column(nullable = false, length=50)
    private String mail;

    @Column(nullable = false, length = 10)
    private Permission permission;

    @Column(length = 15)
    private String cpf;

    @Column(nullable = false, length = 15)
    private LocalDateTime createdAt;

// Getters

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
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

    // Setters

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = UtilHelper.hashPassword(password);
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setPermission(String permission) {
        this.permission = Permission.valueOf(permission);
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    private enum Permission {
        ADMIN,
        USER,
        DEV,
    }
}
