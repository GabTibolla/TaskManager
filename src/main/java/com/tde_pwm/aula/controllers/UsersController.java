package com.tde_pwm.aula.controllers;

import com.tde_pwm.aula.helpers.UtilHelper;
import com.tde_pwm.aula.models.UsersModel;
import com.tde_pwm.aula.repositories.UsersRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class UsersController {

    private final UsersRepository usuarioRepository;

    public UsersController(UsersRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/api/usuarios/{id}")
    public ResponseEntity getUsuario(@PathVariable("id") String id) {
        return usuarioRepository.findById(id)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/api/usuarios")
    public List<UsersModel> getUsuarios() {
        Iterable<UsersModel> usersInterable = usuarioRepository.findAll();
        List<UsersModel> users = new ArrayList<>();

        usersInterable.forEach(users::add);
        // Retorna a lista de usuários
        return users;
    }

    @PostMapping(path = "/api/usuarios")
    public UsersModel insertUser(@RequestBody UsersModel users) {
        users.setCreatedAt(LocalDateTime.now());
        //users.setId(UUID.randomUUID().toString());
        return usuarioRepository.save(users);
    }
}
