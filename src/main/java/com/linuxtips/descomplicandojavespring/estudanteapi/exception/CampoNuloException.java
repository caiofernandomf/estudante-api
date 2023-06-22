package com.linuxtips.descomplicandojavespring.estudanteapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT,reason = "Não são permitidos campos nulos!!!")
public class CampoNuloException extends Exception{

    public CampoNuloException(){

    }

    public CampoNuloException(String mensagem){

        super(mensagem);

    }
}
