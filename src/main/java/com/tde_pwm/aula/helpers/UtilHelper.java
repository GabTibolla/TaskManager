package com.tde_pwm.aula.helpers;

import com.tde_pwm.aula.models.SubTaskModel;
import com.tde_pwm.aula.models.TasksModel;
import com.tde_pwm.aula.models.UsersModel;
import jakarta.persistence.EntityManager;

public class UtilHelper {

    // Função para buscar a SubTask
    public static SubTaskModel buscaSubTask(EntityManager entityManager, int id){
        return entityManager.find(SubTaskModel.class, id);
    }

    // Função para buscar a Task
    public static TasksModel buscaTask(EntityManager entityManager, int id){
        return entityManager.find(TasksModel.class, id);
    }
}
