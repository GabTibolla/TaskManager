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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/task/{idTask}", produces = MediaType.APPLICATION_JSON_VALUE)
public class SubTaskController {

    @Autowired
    EntityManager entityManager;

    @Autowired
    SubTaskRepository subTaskRepository;

    // Função GetAll
    @GetMapping(value = "/subtasks")
    public ResponseEntity<?> getAllSubTasks(@PathVariable int idTask) {

        TasksModel task = UtilHelper.buscaTask(entityManager, idTask);

        // Valida se existe a task
        if (task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(("{\"message\": \"Tarefa inserida não existe\" }"));
        }

        // Busca todos os dados
        Iterable<SubTaskModel> iterableSubTask = subTaskRepository.findAll();
        List<SubTaskModel> subTasks = new ArrayList<>();

        // Percorre a lista de subtasks
        for (SubTaskModel subTask : iterableSubTask) {

            // Só insere as subtasks no array às que forem relativas as tasks
            if (subTask.getTask().getId().equals(idTask)) {
                subTasks.add(subTask);
            }
        }
        // Caso o array esteja vazio
        if (subTasks.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(("{\"message\": \"Não foram encontrados subtasks para esta tarefa\" }"));
        }
        // Retorna o array com dados
        return ResponseEntity.status(HttpStatus.OK).body(subTasks);

    }

    // Função (por id) - Subtask
    @GetMapping(value = "/subtask/{id}")
    public ResponseEntity<?> getSubTask(@PathVariable int idTask, @PathVariable int id) {

        // Busca a task
        TasksModel task = UtilHelper.buscaTask(entityManager, idTask);

        if (task == null) {
            // Task não existe
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(("{\"message\": \"Task inserida não existe\" }"));
        }

        // Busca todos os registros de subtask
        SubTaskModel subTask = UtilHelper.buscaSubTask(entityManager, id);

        // Se não existe a subtask, retorna erro
        if (subTask == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(("{\"message\": \"Subtask não encontrada\" }"));
        }

        // verifica se a task inserida no endpoint é a mesma que está no registro da subtask
        if (!subTask.getTask().getId().equals(idTask)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(("{\"message\": \"Subtask não pertence à task inserida\" }"));
        }
        // Retorna a subtask
        return ResponseEntity.status(HttpStatus.OK).body(subTask);

    }

    // Função PUT - Subtask
    @PutMapping(value = "/subtask/{id}")
    public ResponseEntity<?> updateSubTask(@PathVariable int idTask, @PathVariable int id, @RequestBody SubTaskModel subTask) {
        // Busca todos os registros de subtasks
        SubTaskModel subTaskModel = UtilHelper.buscaSubTask(entityManager, id);

        // Se for nulo, não existe a subtask
        if (subTaskModel == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(("{\"message\": \"SubTask não encontrada\" }"));
        }

        // Verifica se a task no endpoint corresponde à passada no registro da subtask
        if (!subTaskModel.getTask().getId().equals(idTask)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(("{\"message\": \"Subtask não pertence à task inserida\" }"));
        }

        // Verifica se o body contém descrição
        if (subTask.getDescription() != null) {
            subTaskModel.setDescription(subTask.getDescription());
        }

        // Verifica se o body contém timeHours
        if (subTask.getTimeHours() != null) {
            subTaskModel.setTimeHours(subTask.getTimeHours());
        }

        // Atualiza os dados
        subTaskRepository.save(subTaskModel);

        // Retorna OK e a Subtask Atualizada
        return ResponseEntity.status(HttpStatus.OK).body(subTaskModel);
    }

    // Função POST - SubTask
    @PostMapping(value = "/subtask")
    public ResponseEntity<?> subTask(@RequestBody SubTaskModel subTask, @PathVariable int idTask) {

        // Busca a Task
        TasksModel task = UtilHelper.buscaTask(entityManager, idTask);

        // Se a task não existe, retorna
        if (task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(("{\"message\": \"ID da task é inválido\" }"));
        }

        // Atualiza os dados de usuário atribuido
        subTask.setAssignedBy(task.getAssignedByUser());
        subTask.setTask(task);

        // Salva o registro
        SubTaskModel subTaskModel = subTaskRepository.save(subTask);

        // retorna subTask
        return ResponseEntity.status(HttpStatus.CREATED).body(subTaskModel);
    }


    // Função DELETE - SubTask
    @DeleteMapping(value = "/subtask/{id}")
    public ResponseEntity<?> deleteSubTask(@PathVariable int idTask, @PathVariable int id) {
        // Busca a subtask
        SubTaskModel subTask = UtilHelper.buscaSubTask(entityManager, id);

        // Se a subtask for nula, não existe
        if (subTask == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(("{\"message\": \"SubTask não encontrada\" }"));
        }

        // Verifica se o ID da task passada é o mesmo do registro da subtask
        if (!subTask.getTask().getId().equals(idTask)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(("{\"message\": \"Subtask não pertence à task inserida\" }"));
        }

        // Deleta
        subTaskRepository.delete(subTask);

        // Retorna OK
        return ResponseEntity.status(HttpStatus.OK).body(("{\"message\": \"Subtask deletada com sucesso\" }"));
    }

}
