package com.linuxtips.descomplicandojavespring.estudanteapi.exception;

import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class EstudanteExceptionHandler {

    @ExceptionHandler(value = {EstudanteNaoEncontradoException.class})
    public ResponseEntity<Object> handleEstudanteNaoEncontradoException(
            Exception e
    ){
        return
                ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(e.getMessage()+"\n"
                        +e.getClass());
    }

    @ExceptionHandler(value = {EstudanteDuplicadoException.class})
    public ResponseEntity<Object> handleEstudanteDuplicadoException(
            Exception e
    ){
        return
                ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(e.getMessage()+"\n"
                                +e.getClass());
    }

    @ExceptionHandler(value = {ContaDuplicadaException.class})
    public ResponseEntity<Object> handleContaDuplicadaException(
            Exception e
    ){
        return
                ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(e.getMessage()+"\n"
                                +e.getClass());
    }

    @ExceptionHandler(value = {CampoNuloException.class})
    public ResponseEntity<Object> handleCampoNuloException(
            Exception e
    ){
        return
                ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(e.getMessage()+"\n"
                                +e.getClass());
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> handleException(Exception e)
            throws EstudanteNaoEncontradoException,
            EstudanteDuplicadoException,ContaDuplicadaException {

        if (e instanceof DataIntegrityViolationException) {
            DataIntegrityViolationException dataIntegrityViolationException =
                    (DataIntegrityViolationException) e;

            if (dataIntegrityViolationException.getCause() instanceof ConstraintViolationException) {

                if (((ConstraintViolationException) dataIntegrityViolationException.getCause()).getErrorCode() == 23505) {
                    String constraintMessage = dataIntegrityViolationException.getMessage().substring(
                            dataIntegrityViolationException.getMessage().indexOf("constraint"));

                    if (dataIntegrityViolationException.getMessage().contains("CONTA"))
                        return handleContaDuplicadaException(new ContaDuplicadaException("Já existe dados bancários com o número de conta "+ extractValue(constraintMessage)+" cadastrado!!!"));

                    if (dataIntegrityViolationException.getMessage().contains("NOME"))
                        return handleEstudanteDuplicadoException(new EstudanteDuplicadoException("Já existe um estudante com o nome "+ extractValue(constraintMessage)+ " cadastrado"));

                }

                if (dataIntegrityViolationException.getCause() instanceof PropertyValueException) {

                    PropertyValueException propertyValueException = (PropertyValueException) dataIntegrityViolationException.getCause();

                    return handleCampoNuloException(
                            new CampoNuloException(
                                    "Necessário informar o campo "
                                            + propertyValueException.getEntityName()
                                            .substring(
                                                    propertyValueException.getEntityName()
                                                            .lastIndexOf(".") + 1)
                                            + "." + propertyValueException.getPropertyName()));
                }
            }
        }
        return null;
    }

    private String extractValue(String messageError){
        if(messageError.contains("NOME")){
            return messageError.substring(
                    messageError.indexOf("'")-1,
                    messageError.lastIndexOf("'")
            );
        }
        if(messageError.contains("CONTA")){
            return messageError.substring(
                    messageError.indexOf("*/"),
                    messageError.indexOf(")\";")
            ).replaceAll("\\D","");
        }
        return "";
    }
}
