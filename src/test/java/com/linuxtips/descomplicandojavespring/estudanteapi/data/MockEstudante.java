package com.linuxtips.descomplicandojavespring.estudanteapi.data;

import com.linuxtips.descomplicandojavespring.estudanteapi.model.DadosBancarios;
import com.linuxtips.descomplicandojavespring.estudanteapi.model.Estudante;
import com.linuxtips.descomplicandojavespring.estudanteapi.model.TipoContaBancaria;

import java.util.List;

public class MockEstudante {

    public static List<Estudante> mockData(){

        return List.of(
                Estudante.builder()
                        .nome("Caio")
                        .curso("Descomplicando Java e Spring")
                        .endereco("Rua A")
                        .dadosBancarios(
                                DadosBancarios
                                        .builder()
                                        .conta(123)
                                        .agencia(6872)
                                        .digito(1)
                                        .tipoContaBancaria(
                                                TipoContaBancaria.CORRENTE
                                        ).build())
                        .build(),
                Estudante.builder()
                        .nome("Ayrton")
                        .curso("Descomplicando o SQL")
                        .endereco("Lapa")
                        .dadosBancarios(
                                DadosBancarios
                                        .builder()
                                        .conta(4567)
                                        .agencia(123)
                                        .digito(3)
                                        .tipoContaBancaria(
                                                TipoContaBancaria.valueOf("POUPANCA")
                                        ).build())
                        .build()
        );
    }

    public static Estudante dadoParaCriarNovoEstudante(){
        return Estudante.builder()
                .nome("Caio Fernando")
                .curso("Descomplicando o Rust")
                .endereco("Rua B")
                .dadosBancarios(
                        DadosBancarios
                                .builder()
                                .conta(6868)
                                .agencia(6872)
                                .digito(7)
                                .tipoContaBancaria(
                                        TipoContaBancaria.SALARIO
                                ).build())
                .build();
    }

    public static Estudante dadoParaCriarNovoEstudanteComErro(){
        return Estudante.builder()
                .nome("Fernando")
                .curso("Descomplicando o Rust")
                .endereco("Rua B")
                .dadosBancarios(
                        DadosBancarios
                                .builder()
                                .conta(123)
                                .agencia(6872)
                                .digito(7)
                                .tipoContaBancaria(
                                        TipoContaBancaria.SALARIO
                                ).build())
                .build();
    }
}
