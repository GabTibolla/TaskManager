package com.tde_pwm.aula.controllers;

import com.tde_pwm.aula.models.TasksModel;
import com.tde_pwm.aula.models.UsersModel;
import com.tde_pwm.aula.repositories.TasksRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Console;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
public class TasksController {

    private final TasksRepository tasksRepository;

    @Autowired
    EntityManager entityManager;

    public TasksController(TasksRepository repository) {
        this.tasksRepository = repository;
    }

    @GetMapping("/task/{id}")
    public ResponseEntity getTask(@PathVariable(name = "id") Integer id) {
        return tasksRepository.findById(id)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/tasks")
    public List<TasksModel> getTasks() {
        Iterable<TasksModel> tasksInterable = tasksRepository.findAll();
        List<TasksModel> tasks = new ArrayList<>();

        tasksInterable.forEach(tasks::add);

        // Retorna a lista de tarefas
        return tasks;
    }

    @PutMapping("/task/{id}")
    public ResponseEntity<?> updateTask(@PathVariable(name = "id") Integer id, @RequestBody TasksModel task) {
        TasksModel taskModel = tasksRepository.findById(id).orElse(null);

        if (taskModel == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task não encontrada");
        }

        if (task.getDescription() != null){
            taskModel.setDescription(task.getDescription());
        }

        if (task.getStatus() != null){
            taskModel.setStatus(task.getStatus());
        }

        if (task.getTitle() != null){
            taskModel.setTitle(task.getTitle());
        }

        task.setUpdateAt(LocalDateTime.now());

        tasksRepository.save(taskModel);

        return ResponseEntity.ok().body(taskModel);
    }

    @PostMapping("/tasks/{idCreator}/{idAssigned}")
    public ResponseEntity<?> insertTask(@RequestBody TasksModel task, @PathVariable(name = "idCreator") Integer idCreator, @PathVariable(name = "idAssigned") Integer idAssigned) {

        UsersModel userCreator = entityManager.find(UsersModel.class, idCreator);
        UsersModel userAssigned = entityManager.find(UsersModel.class, idAssigned);

        if (userAssigned == null || userCreator == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Usuário criador ou usuário atribuído não encontrado.");
        }

        task.setAssignedByUser(userAssigned);
        task.setCreatedByUser(userCreator);

        return ResponseEntity.status(HttpStatus.CREATED).body(tasksRepository.save(task));
    }

    // Função DELETE - Task
    @DeleteMapping("/task/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable(name = "id") Integer id) {
        TasksModel task = tasksRepository.findById(id).orElse(null);
        if (task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task não encontrada");
        }

        tasksRepository.delete(task);

        return ResponseEntity.status(HttpStatus.OK).body("Task excluida com sucesso");
    }

}
