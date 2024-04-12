package com.tde_pwm.aula.controllers;

import com.tde_pwm.aula.helpers.UtilHelper;
import com.tde_pwm.aula.models.UsersModel;
import com.tde_pwm.aula.repositories.UsersRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
public class UsersController {

    private final UsersRepository usuarioRepository;

    public UsersController(UsersRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Função Get (Por ID) - Usuário
    @GetMapping("/usuarios/{id}")
    public ResponseEntity getUsuario(@PathVariable("id") Integer id) {
        return usuarioRepository.findById(id)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Função Get (Todos) - Usuário
    @GetMapping("/usuarios")
    public List<UsersModel> getUsuarios() {
        Iterable<UsersModel> usersInterable = usuarioRepository.findAll();
        List<UsersModel> users = new ArrayList<>();

        usersInterable.forEach(users::add);

        return users;
    }

    // Função POST - Usuário
    @PostMapping(path = "/usuarios")
    public ResponseEntity<?> insertUser(@RequestBody UsersModel users) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioRepository.save(users));
    }
}
