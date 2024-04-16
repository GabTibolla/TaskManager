package com.tde_pwm.aula.controllers;

import com.tde_pwm.aula.models.TasksModel;
import com.tde_pwm.aula.models.UsersModel;
import com.tde_pwm.aula.repositories.TasksRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Console;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/user/{idUser}", produces = MediaType.APPLICATION_JSON_VALUE)
public class TasksController {

    private final TasksRepository tasksRepository;

    @Autowired
    EntityManager entityManager;

    public TasksController(TasksRepository repository) {
        this.tasksRepository = repository;
    }

    // Get (por id) - TASK
    @GetMapping("/task/{id}")
    public ResponseEntity<?> getTask(@PathVariable int idUser, @PathVariable(name = "id") Integer id) {
        // Buscando Task por ID
        TasksModel tasksModel = tasksRepository.findById(id).orElse(null);

        // Buscando usuário
        UsersModel user = entityManager.find(UsersModel.class, idUser);

        // verifica se a task existe
        if (tasksModel == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(("{\"message\": \"Task não encontrada\" }"));
        }

        // Verifica se existe usuário
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(("{\"message\": \"Usuário não encontrado\" }"));
        }

        // Se a permissão for User só pode ver a task que ele mesmo criou
        if (user.getPermission().equals("USER")) {
            if (tasksModel.getCreatedByUser().getId().equals(idUser)) {
                // Se for a task
                return ResponseEntity.status(HttpStatus.OK).body(tasksModel);
            }
            else{
                // Se a task não for do usuário que pesquisou
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(("{\"message\": \"Task não é acessível à sua permissão\" }"));
            }
        }

        // retorna a task
        return ResponseEntity.status(HttpStatus.OK).body(tasksModel);
    }

    // Get All - TASK
    @GetMapping("/tasks")
    public ResponseEntity<?> getTasks(@PathVariable int idUser) {
        // Buscando tasks
        Iterable<TasksModel> tasksInterable = tasksRepository.findAll();
        List<TasksModel> tasks = new ArrayList<>();

        // Buscando usuário
        UsersModel user = entityManager.find(UsersModel.class, idUser);

        if (user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(("{\"message\": \"Usuário não encontrado\" }"));
        }

        // Permissão de usuário
        if (user.getPermission().equals("USER")) {
            // Só adiciona os quer foram criados pelo próprio
            tasksInterable.forEach(record -> {
                if (record.getCreatedByUser().getId().equals(idUser)) {
                    tasks.add(record);
                }
            });

        } else {
            // Percorre a lista de tasks encontradas
            tasksInterable.forEach(tasks::add);
        }

        // Retorna que não há tasks (se for vazio)
        if (tasks.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(("{\"message\": \"Não há tasks para exibir\" }"));
        }

        // Retorna a lista de tarefas
        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

    // PUT - Tasks
    @PutMapping("/task/{id}")
    public ResponseEntity<?> updateTask(@PathVariable(name = "id") Integer id, @PathVariable(name="idUser") Integer idUser, @RequestBody TasksModel task) {
        // Buscando Task
        TasksModel taskModel = tasksRepository.findById(id).orElse(null);

        // Busca usuário
        UsersModel user = entityManager.find(UsersModel.class, idUser);

        // Verifica se a task existe
        if (taskModel == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(("{\"message\": \"Task não encontrada\" }"));
        }

        // Verifica se o usuário existe
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(("{\"message\": \"Usuário não encontrado\" }"));
        }

        // verifica se o usuário criador tem permissão para alterar a task
        if (!user.getPermission().equals("ADMIN") && !user.getPermission().equals("DEV")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(("{\"message\": \"Usuário não possui as permissões para modificar a task\" }"));
        }

        // Verifica se veio mudança de descrição no body
        if (task.getDescription() != null) {
            taskModel.setDescription(task.getDescription());
        }

        // Verifica se veio mudança do status no body
        if (task.getStatus() != null) {
            taskModel.setStatus(task.getStatus());
        }

        // Verifica se veio mudança do titulo no body
        if (task.getTitle() != null) {
            taskModel.setTitle(task.getTitle());
        }

        // Atualiza a data e Hora de modificação
        taskModel.setUpdateAt(LocalDateTime.now());

        // Salva os dados
        tasksRepository.save(taskModel);

        // retorna
        return ResponseEntity.ok().body(taskModel);
    }


    // Post - TASK
    @PostMapping("/task/assigned/{idAssigned}")
    public ResponseEntity<?> insertTask(@RequestBody TasksModel task, @PathVariable Integer idUser, @PathVariable(name = "idAssigned") Integer idAssigned) {

        // Busca usuário criador e usuário designado
        UsersModel userCreator = entityManager.find(UsersModel.class, idUser);
        UsersModel userAssigned = entityManager.find(UsersModel.class, idAssigned);

        // Verifica se os usuários existem
        if (userAssigned == null || userCreator == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(("{\"message\": \"Usuário criador ou usuário atribuído não encontrado.\" }"));
        }

        // verifica se o usuário criador tem permissão para criar a task
        if (userCreator.getPermission().equals("DEV")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(("{\"message\": \"Usuário criador não possui as permissões para criação de task\" }"));
        }

        // Se o usuário que resolverá a task não for admin ou dev, retorna erro
        if (userAssigned.getPermission().equals("USER")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(("{\"message\": \"Usuário designado não possui as permissões para ser atribuido à task\" }"));
        }

        // Seta os usuarios
        task.setAssignedByUser(userAssigned);
        task.setCreatedByUser(userCreator);

        // Retorna sucesso na criação
        return ResponseEntity.status(HttpStatus.CREATED).body(tasksRepository.save(task));
    }

    // Função DELETE - Task
    @DeleteMapping("/task/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable(name="idUser") Integer idUser, @PathVariable(name = "id") Integer id) {
        // Busca a task pelo ID
        TasksModel task = tasksRepository.findById(id).orElse(null);

        // Busca usuário
        UsersModel user = entityManager.find(UsersModel.class, idUser);

        // Verifica se a task existe
        if (task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(("{\"message\": \"Task não encontrada\" }"));
        }

        // Verifica se o usuário existe
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(("{\"message\": \"Usuário não encontrado\" }"));
        }

        // verifica se o usuário criador tem permissão para deletar a task
        if (!user.getPermission().equals("ADMIN")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(("{\"message\": \"Usuário não possui as permissões para modificar a task\" }"));
        }

        // Deleta a task
        tasksRepository.delete(task);

        // Retorna sucesso
        return ResponseEntity.status(HttpStatus.OK).body(("{\"message\": \"Task excluida com sucesso\" }"));
    }
}
