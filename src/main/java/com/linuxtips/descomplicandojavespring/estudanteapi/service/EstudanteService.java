package com.linuxtips.descomplicandojavespring.estudanteapi.service;

import com.linuxtips.descomplicandojavespring.estudanteapi.exception.EstudanteNaoEncontradoException;
import com.linuxtips.descomplicandojavespring.estudanteapi.model.Estudante;
import com.linuxtips.descomplicandojavespring.estudanteapi.model.mapper.EstudanteMapper;
import com.linuxtips.descomplicandojavespring.estudanteapi.repository.EstudamteRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
@AllArgsConstructor
public class EstudanteService {

    private final EstudamteRepository estudamteRepository;
    private final EstudanteMapper estudanteMapper;

    public Estudante criarEstudante(Estudante estudante)throws SQLException, Exception{
        try{
            return estudamteRepository.save(estudante);
        }catch (Exception e){
            throw e;
                    //new EstudanteDuplicadoException("Estudante com o mesmo nome já cadastrado",e);
        }
    }

    public List<Estudante> listarEstudantes(){
        return estudamteRepository.findAll();
    }

    public ResponseEntity<Estudante> buscarEstudantePorId(long id) throws EstudanteNaoEncontradoException {
        return estudamteRepository.findById(id)
                .map(estudante -> ResponseEntity.ok().body(estudante))
                .orElseThrow(() -> new EstudanteNaoEncontradoException(id));

    }

    public ResponseEntity<Estudante> atualizarEstudantePorId(Estudante estudante,long id)
            throws EstudanteNaoEncontradoException{
        return  estudamteRepository.findById(id)
                .map(estudanteToUpdate ->
                {
                    estudanteMapper.toEstudanteUpdate(estudante, estudanteToUpdate);
                    estudamteRepository.save(estudanteToUpdate);
                    return ResponseEntity.ok().body(estudanteToUpdate);
                }).orElseThrow(() -> new EstudanteNaoEncontradoException(id));

    }

    public ResponseEntity<Object> excluirPorId(long id)throws EstudanteNaoEncontradoException{
        return estudamteRepository.findById(id)
                .map(estudanteToDelete -> {
                    estudamteRepository.delete(estudanteToDelete);
                    return ResponseEntity.noContent().build();
                }).orElseThrow(() -> new EstudanteNaoEncontradoException(id));
    }

    public ResponseEntity<Estudante> buscarEstudantePorNome(String nome) throws EstudanteNaoEncontradoException{
        return
                estudamteRepository.findByName(nome)
                        .map(estudante -> ResponseEntity.ok().body(estudante))
                        .orElseThrow(() -> new EstudanteNaoEncontradoException(nome));
    }

    public List<Estudante> listarEstudantesPorCurso(String curso){
        return estudamteRepository.findByCurso(curso);
    }

    public List<Estudante> listarEstudantesPorNome(String nome){
        return estudamteRepository.findByNomeContains(nome);
    }

    public List<Estudante> listarEstudantesPrimeirosEstudantes(int numero){
        return estudamteRepository.findBy(Example.of(Estudante.builder().build()), q-> q.limit(numero)).all();
    }

}

