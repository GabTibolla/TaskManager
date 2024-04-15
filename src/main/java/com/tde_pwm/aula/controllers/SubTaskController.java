package com.tde_pwm.aula.controllers;

import com.sun.net.httpserver.Authenticator;
import com.tde_pwm.aula.helpers.UtilHelper;
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

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/task/{idTask}")
public class SubTaskController {

    @Autowired
    EntityManager entityManager;

    @Autowired
    SubTaskRepository subTaskRepository;

    // Função GetAll
    @GetMapping(value = "/subtasks")
    public ResponseEntity<?> getAllSubTasks(@PathVariable int idTask) {

        if (UtilHelper.validaTask(entityManager, idTask)) {

            Iterable<SubTaskModel> iterableSubTask = subTaskRepository.findAll();
            List<SubTaskModel> subTasks = new ArrayList<>();

            for (SubTaskModel subTask : iterableSubTask) {
                if (subTask.getTask().getId().equals(idTask)) {
                    subTasks.add(subTask);
                }
            }

            if (subTasks.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foram encontrados subtasks para esta tarefa");
            }
            return ResponseEntity.status(HttpStatus.OK).body(subTasks);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarefa inserida não existe");
    }

    // Função (por id) - Subtask
    @GetMapping(value = "/subtask/{id}")
    public ResponseEntity<?> getSubTask(@PathVariable int idTask, @PathVariable int id) {

        if (UtilHelper.validaTask(entityManager, idTask)) {

            SubTaskModel subTask = subTaskRepository.findById(id).orElse(null);

            if (subTask == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Subtask não encontrada");
            }

            if (!subTask.getTask().getId().equals(idTask)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Subtask não pertence à task inserida");
            }

            return ResponseEntity.status(HttpStatus.OK).body(subTask);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task inserida não existe");
    }

    // Função PUT - Subtask
    @PutMapping(value = "/subtask/{id}")
    public ResponseEntity<?> updateSubTask(@PathVariable int idTask, @PathVariable int id, @RequestBody SubTaskModel subTask) {
        SubTaskModel subTaskModel = subTaskRepository.findById(id).orElse(null);

        if (subTaskModel == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("SubTask não encontrada");
        }

        if (!subTaskModel.getTask().getId().equals(idTask)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Subtask não pertence à task inserida");
        }

        if (subTask.getDescription() != null) {
            subTaskModel.setDescription(subTask.getDescription());
        }

        if (subTask.getTimeHours() != null) {
            subTaskModel.setTimeHours(subTask.getTimeHours());
        }

        subTaskRepository.save(subTaskModel);

        return ResponseEntity.status(HttpStatus.OK).body(subTaskModel);
    }

    // Função POST - SubTask
    @PostMapping(value = "/subtask")
    public ResponseEntity<?> subTask(@RequestBody SubTaskModel subTask, @PathVariable int idTask) {

        TasksModel task = entityManager.find(TasksModel.class, idTask);

        if (task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ID da task é inválido");
        }

        subTask.setAssignedBy(task.getAssignedByUser());
        subTask.setTask(task);

        SubTaskModel subTaskModel = subTaskRepository.save(subTask);

        return ResponseEntity.ok().body(subTaskModel);
    }

    // Função DELETE - SubTask
    @DeleteMapping(value = "/subtask/{id}")
    public ResponseEntity<?> deleteSubTask(@PathVariable int idTask, @PathVariable int id) {
        SubTaskModel subTask = subTaskRepository.findById(id).orElse(null);

        if (subTask == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("SubTask não encontrada");
        }

        if (!subTask.getTask().getId().equals(idTask)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Subtask não pertence à task inserida");
        }

        subTaskRepository.delete(subTask);

        return ResponseEntity.status(HttpStatus.OK).body("Subtask deletada com sucesso");
    }
}
