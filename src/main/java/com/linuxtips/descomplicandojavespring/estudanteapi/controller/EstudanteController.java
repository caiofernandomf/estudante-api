package com.linuxtips.descomplicandojavespring.estudanteapi.controller;

import com.linuxtips.descomplicandojavespring.estudanteapi.model.Estudante;
import com.linuxtips.descomplicandojavespring.estudanteapi.service.EstudanteService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
@AllArgsConstructor
public class EstudanteController {
    private final EstudanteService estudanteService;

    @SneakyThrows
    @PostMapping("/estudantes")
    @ResponseStatus(HttpStatus.CREATED)
    public Estudante criarEstudante(@RequestBody Estudante estudante){
        return estudanteService.criarEstudante(estudante);
    }

    @GetMapping("/estudantes")
    @ResponseStatus(HttpStatus.OK)
    public List<Estudante> listarEstudantes(){
        return estudanteService.listarEstudantes();

    }
    @SneakyThrows
    @GetMapping("/estudantes/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Estudante> listarPorId(@PathVariable long id){
        return estudanteService.buscarEstudantePorId(id);
    }

    @SneakyThrows
    @PutMapping("/estudantes/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Estudante> atuallizarPorId(@PathVariable long id, @RequestBody Estudante estudante){
        return estudanteService.atualizarEstudantePorId(estudante,id);
    }

    @SneakyThrows
    @DeleteMapping("/estudantes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Object> excluirPorId(@PathVariable long id){
        return estudanteService.excluirPorId(id);
    }

    @SneakyThrows
    @GetMapping("/estudantes/nome/{nome}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Estudante> buscarPorNome(@PathVariable String nome){
        return estudanteService.buscarEstudantePorNome(nome);
    }

    @GetMapping("/estudantes/curso")
    @ResponseStatus(HttpStatus.OK)
    public List<Estudante> buscarPorCurso(@RequestParam(required = true) String curso){
        return estudanteService.listarEstudantesPorCurso(curso);
    }

    @GetMapping("/estudantes/nome")
    @ResponseStatus(HttpStatus.OK)
    public List<Estudante> listarPorNome(@RequestParam(required = true) String nome){
        return estudanteService.listarEstudantesPorNome(nome);
    }

    @GetMapping("/estudantes/promo")
    @ResponseStatus(HttpStatus.OK)
    public List<Estudante> listarPrimeiros(@RequestParam(required = true) int numero){
        return estudanteService.listarEstudantesPrimeirosEstudantes(numero);
    }
}
