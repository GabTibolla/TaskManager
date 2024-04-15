package com.tde_pwm.aula.helpers;

import com.tde_pwm.aula.models.TasksModel;
import com.tde_pwm.aula.models.UsersModel;
import jakarta.persistence.EntityManager;

public class UtilHelper {

    public static boolean validaTask(EntityManager entityManager, int id){
        TasksModel tasksModel = entityManager.find(TasksModel.class, id);

        return tasksModel != null;
    }

    public static boolean validaUsuario(EntityManager entityManager, int id){
        UsersModel user = entityManager.find(UsersModel.class, id);

        return user != null;
    }
}
