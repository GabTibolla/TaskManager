package com.tde_pwm.aula.repositories;

import com.tde_pwm.aula.models.UsersModel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UsersRepository extends CrudRepository<UsersModel, Integer> {
    Optional<UsersModel> findByMailAndCpf(String mail, String cpf);
    List<UsersModel> findByPermission(UsersModel.Permission permission);
}
