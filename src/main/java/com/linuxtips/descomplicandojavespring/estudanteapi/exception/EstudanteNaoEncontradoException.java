package com.linuxtips.descomplicandojavespring.estudanteapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND,reason = "Estudante não existe na base")
public class EstudanteNaoEncontradoException extends Exception{

    public EstudanteNaoEncontradoException(){
        super();
    }

    public EstudanteNaoEncontradoException(Long id){
        super("Estudante com o id "+id+" não localizado na base");
    }

    public EstudanteNaoEncontradoException(String nome){
        super("Estudante com o nome "+nome+" não localizado na base");
    }
}
