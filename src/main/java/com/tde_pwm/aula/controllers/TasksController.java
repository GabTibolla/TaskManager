package com.tde_pwm.aula.controllers;

import com.tde_pwm.aula.models.TasksModel;
import com.tde_pwm.aula.models.UsersModel;
import com.tde_pwm.aula.repositories.TasksRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TasksController {

    private final TasksRepository tasksRepository;

    public TasksController(TasksRepository repository) {
        this.tasksRepository = repository;
    }

    @GetMapping("/api/task/{id}")
    public ResponseEntity getTask(@PathVariable(name="id") int id) {
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
}
