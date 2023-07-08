package com.linuxtips.descomplicandojavespring.estudanteapi.service;

import com.linuxtips.descomplicandojavespring.estudanteapi.data.MockEstudante;
import com.linuxtips.descomplicandojavespring.estudanteapi.exception.EstudanteNaoEncontradoException;
import com.linuxtips.descomplicandojavespring.estudanteapi.model.Estudante;
import com.linuxtips.descomplicandojavespring.estudanteapi.model.mapper.EstudanteMapperImpl;
import com.linuxtips.descomplicandojavespring.estudanteapi.repository.EstudanteRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class EstudanteServiceTest {

    @Mock
    private EstudanteRepository estudanteRepository;

    @Spy
    private EstudanteMapperImpl estudanteMapper;

    @InjectMocks
    private EstudanteService estudanteService;


    @Test
    @DisplayName("Sucesso - deve salvar estudante na base com sucesso")
    void criarEstudante() throws Exception {
        Estudante estudante = MockEstudante.dadoParaCriarNovoEstudante();
        estudanteService.criarEstudante(estudante);
        Mockito.verify(estudanteRepository).save(estudante);
    }

    @Test
    @DisplayName("Sucesso - deve salvar estudante na base com sucesso e sem lançar excessão")
    void criarEstudanteSemLancarException() throws Exception {
        Estudante estudante = MockEstudante.dadoParaCriarNovoEstudante();
        assertDoesNotThrow(()->estudanteService.criarEstudante(estudante));
        //Mockito.verify(estudanteRepository).save(estudante);
    }

    @Test
    @DisplayName("Erro - deve lançar exception ao tentar salvar estudante duplicado")
    void criarEstudanteDuplicado() throws Exception {
        Estudante estudante0 = MockEstudante.mockData().get(0);
        Estudante estudante = MockEstudante.dadoParaCriarNovoEstudante();
        estudanteService.criarEstudante(estudante0);
        Mockito.when(estudanteRepository.save(estudante)).thenThrow(DataIntegrityViolationException.class);

        assertThrows(DataIntegrityViolationException.class,()-> estudanteService.criarEstudante(estudante));
    }

    @Test
    @DisplayName("Sucesso - deve listar estudantes com sucesso")
    void listarEstudantes() {
        Mockito.when(estudanteRepository.findAll()).thenReturn(MockEstudante.mockData());
        List<Estudante> lista = estudanteService.listarEstudantes();
        assertTrue(lista.size() > 0);

        Mockito.verify(estudanteRepository).findAll();

    }

    @Test
    @DisplayName("Sucesso - deve buscar estudante por id com sucesso")
    void buscarEstudantePorId() throws EstudanteNaoEncontradoException {
        var estudante = MockEstudante.dadoParaBuscarEstudante();

        Mockito.when(
                estudanteRepository
                        .findById(
                                estudante.getId()))
                .thenReturn(Optional.of(estudante));

        var estudanteBase = estudanteService.buscarEstudantePorId(estudante.getId());

        assertEquals(estudanteBase.getBody().getId(),estudante.getId());

        Mockito.verify(estudanteRepository).findById(Mockito.any());

    }

    @Test
    @DisplayName("Erro - deve buscar estudante por id não existente")
    void buscarEstudantePorIdInexistente() throws EstudanteNaoEncontradoException {
        var idEstudante = 35l;

        Mockito.when(
                        estudanteRepository
                                .findById(
                                        idEstudante))
                .thenReturn(Optional.empty());


        assertThrows(EstudanteNaoEncontradoException.class,
                ()->estudanteService.buscarEstudantePorId(idEstudante));

        Mockito.verify(estudanteRepository).findById(Mockito.any());

    }

    @Test
    @DisplayName("Sucesso - atualiza estudante por ID")
    void atualizarEstudantePorId() throws EstudanteNaoEncontradoException {
        var estudante = MockEstudante.dadoParaBuscarEstudante();
        var estudanteToUpdate = MockEstudante.dadoParaCriarNovoEstudante();

        Mockito.when(
                        estudanteRepository
                                .findById(
                                        estudante.getId()))
                .thenReturn(Optional.of(MockEstudante.dadoParaBuscarEstudante()));

        var responseEntity=estudanteService.atualizarEstudantePorId(estudanteToUpdate,estudante.getId());

        Mockito.verify(estudanteRepository).findById(Mockito.any());

        Mockito.verify(estudanteRepository).save(Mockito.any(Estudante.class));

        var estudanteAtualizado = responseEntity.getBody();

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertNotEquals(estudanteAtualizado,estudante);

    }

    @Test
    @DisplayName("Sucesso - deve excluir estudante sem lançar exception")
    void excluirPorId() {
        var estudante = MockEstudante.dadoParaBuscarEstudante();

        Mockito.when(
                        estudanteRepository
                                .findById(
                                        estudante.getId()))
                .thenReturn(Optional.of(estudante));

        var responseEntity= assertDoesNotThrow(()->estudanteService.excluirPorId(estudante.getId()));

        Mockito.verify(estudanteRepository).findById(Mockito.any());

        Mockito.verify(estudanteRepository).delete(Mockito.any(Estudante.class));

        assertEquals(responseEntity.getStatusCode(), HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("Sucesso - deve retornar estudante por nome")
    void buscarEstudantePorNome() {
        var estudante = MockEstudante.dadoParaBuscarEstudante();

        Mockito.when(
                        estudanteRepository
                                .findByName("Fernando"))
                .thenReturn(Optional.of(estudante));

        var responseEntity= assertDoesNotThrow(()->estudanteService.buscarEstudantePorNome("Fernando"));

        Mockito.verify(estudanteRepository).findByName("Fernando");

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);

        assertEquals(estudante,responseEntity.getBody());
    }

    private List<Estudante> configListaBuscaPorCurso(String curso){
        List<Estudante> lista = new ArrayList<>(MockEstudante.mockData());
        lista.add(MockEstudante.dadoParaCriarNovoEstudanteComErro());
        lista.add(MockEstudante.dadoParaBuscarEstudante());
        lista.add(MockEstudante.dadoParaCriarNovoEstudante());

        return
                lista.stream()
                        .filter(estudante -> estudante.getCurso().equals(curso))
                        .collect(Collectors.toList());
    }
    @Test
    @DisplayName("Sucesso - deve retornar lista de estudantes por curso")
    void listarEstudantesPorCurso() {
        var curso = "Descomplicando o Go";
        Mockito.when(
                        estudanteRepository
                                .findByCurso(curso))
                .thenReturn(configListaBuscaPorCurso(curso));

        var lista= assertDoesNotThrow(()->estudanteService.listarEstudantesPorCurso(curso));

        Mockito.verify(estudanteRepository).findByCurso(curso);

        assertTrue(lista.size() > 0);

        System.out.println(lista);
    }

    private List<Estudante> configListaBuscaPorNome(String nome){
        List<Estudante> lista = new ArrayList<>(MockEstudante.mockData());
        lista.add(MockEstudante.dadoParaCriarNovoEstudanteComErro());
        lista.add(MockEstudante.dadoParaBuscarEstudante());
        lista.add(MockEstudante.dadoParaCriarNovoEstudante());

        return
                lista.stream()
                        .filter(estudante -> estudante.getNome().contains(nome))
                        .collect(Collectors.toList());
    }
    @Test
    @DisplayName("Sucesso = deve retornar estudantes que contém determinado nome")
    void listarEstudantesPorNome() {
        var nome = "Fernando";
        Mockito.when(
                        estudanteRepository
                                .findByNomeContains(nome))
                .thenReturn(configListaBuscaPorNome(nome));

        var lista= assertDoesNotThrow(()->estudanteService.listarEstudantesPorNome(nome));

        Mockito.verify(estudanteRepository).findByNomeContains(nome);

        assertTrue(lista.size() > 0);

        System.out.println(lista);
    }

    @Test
    @DisplayName("Sucesso - deve listar os primeiros X estudantes")
    void listarEstudantesPrimeirosEstudantes() {
        var limite = 2;
        Mockito.when(
                        estudanteRepository
                                .findTops(limite))
                .thenReturn(MockEstudante.mockData());

        var lista= assertDoesNotThrow(()->estudanteService.listarEstudantesPrimeirosEstudantes(limite));

        Mockito.verify(estudanteRepository).findTops(limite);

        assertTrue(lista.size() > 0);

        System.out.println(lista);
    }
}