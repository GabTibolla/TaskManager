package com.tde_pwm.aula.controllers;

import com.tde_pwm.aula.models.TasksModel;
import com.tde_pwm.aula.models.UsersModel;
import com.tde_pwm.aula.repositories.TasksRepository;
import com.tde_pwm.aula.repositories.UsersRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/api/task/{id}")
    public ResponseEntity getTask(@PathVariable(name="id") Integer id) {
      return tasksRepository.findById(id)
              .map(record -> ResponseEntity.ok().body(record))
              .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/api/tasks")
    public List<TasksModel> getTasks() {
        Iterable<TasksModel> tasksInterable = tasksRepository.findAll();
        List<TasksModel> tasks = new ArrayList<>();

        tasksInterable.forEach(tasks::add);

        // Retorna a lista de tarefas
        return tasks;
    }

    @PostMapping("api/tasks/{idCreator}/{idAssigned}")
    public TasksModel insertTask(@RequestBody TasksModel task, @PathVariable(name = "idCreator") Integer idCreator, @PathVariable(name="idAssigned") Integer idAssigned) {

        UsersModel userCreator = entityManager.find(UsersModel.class, idCreator);
        UsersModel userAssigned = entityManager.find(UsersModel.class, idAssigned);

        task.setAssignedByUser(userAssigned);
        task.setCreatedByUser(userCreator);

        return tasksRepository.save(task);
    }
}
