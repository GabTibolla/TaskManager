package com.tde_pwm.aula.controllers;

import com.tde_pwm.aula.models.UsersModel;
import com.tde_pwm.aula.repositories.UsersRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class UsersController {

    private final UsersRepository usuarioRepository;
    private final UsersRepository usersRepository;

    public UsersController(UsersRepository usuarioRepository, UsersRepository usersRepository) {
        this.usuarioRepository = usuarioRepository;
        this.usersRepository = usersRepository;
    }

    // Função Get (Por ID) - Usuário
    @GetMapping("/usuario/{id}")
    public ResponseEntity<?> getUsuario(@PathVariable("id") Integer id) {
        // Buscando usuario
        UsersModel user = usuarioRepository.findById(id).orElse(null);

        // Se for nulo, retorna que não encontrou
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(("{\"message\": \"Usuário não encontrado\" }"));
        }

        // Retorna o usuário
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @GetMapping("/usuarioLogin/{mail}/{cpf}")
    public ResponseEntity<?> getUsuarioByEmailAndCpf(@PathVariable String mail, @PathVariable String cpf) {
        // Buscando usuário pelo email e CPF
        UsersModel user = usuarioRepository.findByMailAndCpf(mail, cpf).orElse(null);

        // Se for nulo, retorna que não encontrou
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\": \"Usuário não encontrado\" }");
        }

        // Retorna o usuário
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }



    // Função Get (Todos) - Usuário
    @GetMapping("/usuarios")
    public ResponseEntity<?> getUsuarios() {
        // Buscando todos os usuários
        Iterable<UsersModel> usersInterable = usuarioRepository.findAll();
        List<UsersModel> users = new ArrayList<>();

        // Percorre os usuários e insere no vetor
        usersInterable.forEach(users::add);

        // Se não houver usuários, retorna
        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(("{\"message\": \"Não há usuários para exibir\" }"));
        }

        // retorna os usuários
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    // Função PUT - Usuário
    @PutMapping(path = "@user/{idUser}/usuario/{id}")
    public ResponseEntity<?> updateUsuario(@PathVariable int idUser, @RequestBody UsersModel usersModel, @PathVariable("id") Integer id) {
        // Buscando o usuário pelo ID
        UsersModel user = usuarioRepository.findById(id).orElse(null);

        // Busca id do usuário que vai modificar
        UsersModel userManager= usersRepository.findById(idUser).orElse(null);

        // Verifica se existe o usuário
        if (user == null || userManager == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(("{\"message\": \"Usuário não encontrado\" }"));

        }

        if (!userManager.getPermission().equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(("{\"message\": \"Usuário não tem as permissões necessárias\" }"));
        }

        // Verifica se o nome veio no body
        if (usersModel.getName() != null) {
            user.setName(usersModel.getName());
        }

        // Verifica se a alteração de permissão veio no body
        if (usersModel.getPermission() != null){
            user.setPermission(usersModel.getPermission());
        }

        // Atualiza a hora de edição do usuário
        user.setUpdatedAt(LocalDateTime.now());

        // salva com os dados enviados pelo json
        usersModel = usersRepository.save(user);

        // retorna
        return ResponseEntity.ok().body(usersModel);
    }

    // Função POST - Usuário
    @PostMapping(path = "/usuario")
    public ResponseEntity<?> insertUser(@RequestBody UsersModel users) {
        // Retorna o usuário criado
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioRepository.save(users));
    }

    // Função DELETE - Usuário
    @DeleteMapping("/user/{idUser}/usuario/{id}")
    public ResponseEntity<?> deleteUsuario(@PathVariable int idUser, @PathVariable("id") Integer id) {
        // Buscando usuario
        UsersModel user = usuarioRepository.findById(id).orElse(null);

        // Busca id do usuário que vai modificar
        UsersModel userManager= usersRepository.findById(idUser).orElse(null);

        // Verifica se o usuário existe
        if (user == null || userManager == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(("{\"message\": \"Usuário não encontrado\" }"));
        }

        if (!userManager.getPermission().equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(("{\"message\": \"Usuário não tem as permissões necessárias\" }"));
        }

        // Deleta, se existir
        usuarioRepository.delete(user);

        // Retorna OK
        return ResponseEntity.status(HttpStatus.OK).body(("{\"message\": \"Usuário excluído com sucesso\" }"));
    }
}
