package com.tde_pwm.aula.repositories;

import com.tde_pwm.aula.models.UsersModel;
import org.springframework.data.repository.CrudRepository;

public interface UsersRepository extends CrudRepository<UsersModel, Integer> {

}
