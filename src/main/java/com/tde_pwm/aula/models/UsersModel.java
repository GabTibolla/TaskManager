package com.tde_pwm.aula.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.tde_pwm.aula.helpers.UtilHelper;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity(name = "users")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class UsersModel {

    public UsersModel() {}

    // Colunas
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable=false, length=50)
    private String name;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false, length = 40)
    private String password;

    @Column(nullable = false, length=50)
    private String mail;

    @Column(nullable = false, length = 10)
    private Permission permission;

    @Column(length = 15)
    private String cpf;

    @CreationTimestamp
    @Column(nullable = false, length = 15)
    private LocalDateTime createdAt;

// Getters

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @JsonIgnore
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


    public void setId(Integer id) {
        this.id = id;
    }

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
        this.permission = Permission.valueOf(permission.toUpperCase());
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
