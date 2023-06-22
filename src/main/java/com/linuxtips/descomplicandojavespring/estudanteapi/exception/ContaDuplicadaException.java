package com.linuxtips.descomplicandojavespring.estudanteapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT,reason = "Conta duplicada - Já existe conta bancária cadastrada com esse número")
public class ContaDuplicadaException extends Exception{
    public ContaDuplicadaException(){
        super();
    }

    public ContaDuplicadaException(String mensagem){
        super(mensagem);
    }
}
