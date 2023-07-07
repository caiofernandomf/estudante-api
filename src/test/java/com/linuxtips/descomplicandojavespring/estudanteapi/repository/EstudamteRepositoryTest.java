package com.linuxtips.descomplicandojavespring.estudanteapi.repository;

import com.linuxtips.descomplicandojavespring.estudanteapi.data.MockEstudante;
import com.linuxtips.descomplicandojavespring.estudanteapi.model.Estudante;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;


@DataJpaTest
//@DataJpaTest
public class EstudamteRepositoryTest {
    @Autowired
    EstudamteRepository estudamteRepository;
    List<Estudante> listaDeEstudantes;
    @BeforeEach
    public void iniciarRepository(){
        listaDeEstudantes = estudamteRepository.saveAll(MockEstudante.mockData());
    }
    @Test
    @DisplayName("Sucesso - Deve Listar todos os estudantes")
    void listarEstudantes(){
        List<Estudante> lista = estudamteRepository.findAll();
        Assertions.assertTrue(lista.size() > 0);

    }

    private Estudante insereEstudante(){
        return estudamteRepository.save(MockEstudante.dadoParaCriarNovoEstudante());
    }
    @Test
    @DisplayName("Sucesso - Deve inserir um novo registro")
    void inserirEstudante(){
       Estudante estudante = insereEstudante();
       Assertions.assertTrue(estudante.getId()>0);
    }


    @Test
    @DisplayName("Erro - Deve retornar uma exception ao tentar inserir um novo registro")
    void inserirEstudanteComErro(){
        Estudante estudante = estudamteRepository.save(MockEstudante.dadoParaCriarNovoEstudante());

        Assertions.assertThrows(DataIntegrityViolationException.class,()->{
            estudamteRepository.save(MockEstudante.dadoParaCriarNovoEstudanteComErro());
        });

    }

    @Test
    @DisplayName("Sucesso - Deve excluir estudante na base com sucesso")
    void excluirEstudanteComSucesso(){
        Long idEstudante = listaDeEstudantes.get(0).getId();
        estudamteRepository.deleteById(idEstudante);

        Optional<Estudante> optional = estudamteRepository.findById(idEstudante);

        Assertions.assertFalse(optional.isPresent());
    }

    @Test
    @DisplayName("Sucesso - Deve buscar estudande pelo nome")
    void buscarEstudantePorNome(){
        Optional<Estudante> optional = estudamteRepository.findByName("Caio");

        Assertions.assertTrue(optional.isPresent());

    }

    @Test
    @DisplayName("Sucesso - Deve buscar estudande pelo nome e curso")
    void buscarEstudantePorNomeEhCurso(){
        List<Estudante> optional = estudamteRepository.findByNomeStartsWithAndCurso("Ayrton","Descomplicando o SQL");

        Assertions.assertTrue(optional.size()>0);

    }

    @Test
    @DisplayName("Sucesso - Deve buscar todos os estudande que contém nome")
    void buscarEstudanteContemNome(){
        insereEstudante();
        List<Estudante> optional = estudamteRepository.findByNomeContains("Caio");

        Assertions.assertTrue(optional.size()>0);

    }

    @Test
    @DisplayName("Sucesso - Deve buscar todos os estudande por Curso")
    void buscarEstudantePorCurso(){
        insereEstudante();
        List<Estudante> optional = estudamteRepository.findByCurso("Descomplicando o Rust");

        Assertions.assertTrue(optional.size()>0);

    }
}