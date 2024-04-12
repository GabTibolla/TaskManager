package com.tde_pwm.aula.controllers;

import com.sun.net.httpserver.Authenticator;
import com.tde_pwm.aula.models.SubTaskModel;
import com.tde_pwm.aula.models.TasksModel;
import com.tde_pwm.aula.models.UsersModel;
import com.tde_pwm.aula.repositories.SubTaskRepository;
import jakarta.persistence.EntityManager;
import org.apache.coyote.Response;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/task/{idTask}")
public class SubTaskController {

    @Autowired
    EntityManager entityManager;

    @Autowired
    SubTaskRepository subTaskRepository;

    @PostMapping(value="/subtask")
    public ResponseEntity<?> subTask(@RequestBody SubTaskModel subTask, @PathVariable int idTask) {

        TasksModel task = entityManager.find(TasksModel.class, idTask);

        if (task == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ID da task é inválido");
        }

        subTask.setAssignedBy(task.getAssignedByUser());
        subTask.setTask(task);

        SubTaskModel subTaskModel = subTaskRepository.save(subTask);

        return ResponseEntity.ok().body(subTaskModel);
    }
}
