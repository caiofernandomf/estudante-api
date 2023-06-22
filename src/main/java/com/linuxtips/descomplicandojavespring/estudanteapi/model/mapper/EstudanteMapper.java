package com.linuxtips.descomplicandojavespring.estudanteapi.model.mapper;

import com.linuxtips.descomplicandojavespring.estudanteapi.model.DadosBancarios;
import com.linuxtips.descomplicandojavespring.estudanteapi.model.Estudante;
import org.mapstruct.*;
@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface EstudanteMapper {

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "nome" ,nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    //@Mapping(target = "meioPagamento" ,conditionExpression = "java(estudante.getMeioPagamento()>0 )")
    @Mapping(target = "endereco" ,nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "curso" ,nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "dadosBancarios.id" ,nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "dadosBancarios.agencia" ,nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "dadosBancarios.conta" ,nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "dadosBancarios.digito" ,nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "dadosBancarios.tipoContaBancaria" ,nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "dadosBancarios.estudante" ,ignore = true)
    void toEstudanteUpdate(Estudante estudante, @MappingTarget Estudante estudanteRetorno );

    @AfterMapping
    default  void setEstudante(@MappingTarget Estudante estudante){
        estudante.getDadosBancarios().setEstudante(estudante);
    }

}
