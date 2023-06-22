package com.linuxtips.descomplicandojavespring.estudanteapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT,reason = "Estudante duplicado - Já existe um estudante com esse nome")
public class EstudanteDuplicadoException extends Exception{

    public EstudanteDuplicadoException(){
        super();
    }

    public EstudanteDuplicadoException(String mensagem){
        super(mensagem);
    }

}
