package com.tde_pwm.aula.expections;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalException {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public String illegalArgumentExceptionHandler(IllegalArgumentException ex) {
        String errorMessage = ex.getMessage();

        // Extrai o valor que não foi encontrado da mensagem de erro
        String enumConstant = errorMessage.substring(errorMessage.lastIndexOf('.') + 1);
        return enumConstant + " não é um tipo válido";
    }
}
