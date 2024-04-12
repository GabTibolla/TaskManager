package com.tde_pwm.aula.repositories;

import com.tde_pwm.aula.models.TasksModel;
import org.springframework.data.repository.CrudRepository;

public interface TasksRepository extends CrudRepository<TasksModel, Integer> {
}
