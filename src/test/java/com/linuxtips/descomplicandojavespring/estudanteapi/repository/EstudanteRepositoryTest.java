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
public class EstudanteRepositoryTest {
    @Autowired
    EstudanteRepository estudanteRepository;
    List<Estudante> listaDeEstudantes;
    @BeforeEach
    public void iniciarRepository(){
        listaDeEstudantes = estudanteRepository.saveAll(MockEstudante.mockData());
    }
    @Test
    @DisplayName("Sucesso - Deve Listar todos os estudantes")
    void listarEstudantes(){
        List<Estudante> lista = estudanteRepository.findAll();
        Assertions.assertTrue(lista.size() > 0);

    }

    private Estudante insereEstudante(){
        return estudanteRepository.save(MockEstudante.dadoParaCriarNovoEstudante());
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
        Estudante estudante = estudanteRepository.save(MockEstudante.dadoParaCriarNovoEstudante());

        Assertions.assertThrows(DataIntegrityViolationException.class,()->{
            estudanteRepository.save(MockEstudante.dadoParaCriarNovoEstudanteComErro());
        });

    }

    @Test
    @DisplayName("Sucesso - Deve excluir estudante na base com sucesso")
    void excluirEstudanteComSucesso(){
        Long idEstudante = listaDeEstudantes.get(0).getId();
        estudanteRepository.deleteById(idEstudante);

        Optional<Estudante> optional = estudanteRepository.findById(idEstudante);

        Assertions.assertFalse(optional.isPresent());
    }

    @Test
    @DisplayName("Sucesso - Deve buscar estudande pelo nome")
    void buscarEstudantePorNome(){
        Optional<Estudante> optional = estudanteRepository.findByName("Caio");

        Assertions.assertTrue(optional.isPresent());

    }

    @Test
    @DisplayName("Sucesso - Deve buscar estudande pelo nome e curso")
    void buscarEstudantePorNomeEhCurso(){
        List<Estudante> optional = estudanteRepository.findByNomeStartsWithAndCurso("Ayrton","Descomplicando o SQL");

        Assertions.assertTrue(optional.size()>0);

    }

    @Test
    @DisplayName("Sucesso - Deve buscar todos os estudande que contém nome")
    void buscarEstudanteContemNome(){
        insereEstudante();
        List<Estudante> optional = estudanteRepository.findByNomeContains("Caio");

        Assertions.assertTrue(optional.size()>0);

    }

    @Test
    @DisplayName("Sucesso - Deve buscar todos os estudande por Curso")
    void buscarEstudantePorCurso(){
        insereEstudante();
        List<Estudante> optional = estudanteRepository.findByCurso("Descomplicando o Rust");

        Assertions.assertTrue(optional.size()>0);

    }

    @Test
    @DisplayName("Sucesso - Deve buscar todos os x primeiros estudantes")
    void buscarPrimeirosEstudantes(){
        insereEstudante();

        List<Estudante> optional = estudanteRepository.findTops(2);

        optional.forEach(System.out::println);
        Assertions.assertTrue(optional.size()>0);

    }
}