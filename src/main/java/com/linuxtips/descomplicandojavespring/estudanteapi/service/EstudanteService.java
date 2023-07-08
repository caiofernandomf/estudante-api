package com.linuxtips.descomplicandojavespring.estudanteapi.service;

import com.linuxtips.descomplicandojavespring.estudanteapi.exception.EstudanteNaoEncontradoException;
import com.linuxtips.descomplicandojavespring.estudanteapi.model.Estudante;
import com.linuxtips.descomplicandojavespring.estudanteapi.model.mapper.EstudanteMapper;
import com.linuxtips.descomplicandojavespring.estudanteapi.repository.EstudanteRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
@AllArgsConstructor
public class EstudanteService {

    private final EstudanteRepository estudanteRepository;
    private final EstudanteMapper estudanteMapper;

    public Estudante criarEstudante(Estudante estudante)throws SQLException, Exception{
        try{
            return estudanteRepository.save(estudante);
        }catch (Exception e){
            throw e;
                    //new EstudanteDuplicadoException("Estudante com o mesmo nome já cadastrado",e);
        }
    }

    public List<Estudante> listarEstudantes(){
        return estudanteRepository.findAll();
    }

    public ResponseEntity<Estudante> buscarEstudantePorId(long id) throws EstudanteNaoEncontradoException {
        return estudanteRepository.findById(id)
                .map(estudante -> ResponseEntity.ok().body(estudante))
                .orElseThrow(() -> new EstudanteNaoEncontradoException(id));

    }

    public ResponseEntity<Estudante> atualizarEstudantePorId(Estudante estudante,long id)
            throws EstudanteNaoEncontradoException{
        return  estudanteRepository.findById(id)
                .map(estudanteToUpdate ->
                {
                    estudanteMapper.toEstudanteUpdate(estudante, estudanteToUpdate);
                    estudanteRepository.save(estudanteToUpdate);
                    return ResponseEntity.ok().body(estudanteToUpdate);
                }).orElseThrow(() -> new EstudanteNaoEncontradoException(id));

    }

    public ResponseEntity<Object> excluirPorId(long id)throws EstudanteNaoEncontradoException{
        return estudanteRepository.findById(id)
                .map(estudanteToDelete -> {
                    estudanteRepository.delete(estudanteToDelete);
                    return ResponseEntity.noContent().build();
                }).orElseThrow(() -> new EstudanteNaoEncontradoException(id));
    }

    public ResponseEntity<Estudante> buscarEstudantePorNome(String nome) throws EstudanteNaoEncontradoException{
        return
                estudanteRepository.findByName(nome)
                        .map(estudante -> ResponseEntity.ok().body(estudante))
                        .orElseThrow(() -> new EstudanteNaoEncontradoException(nome));
    }

    public List<Estudante> listarEstudantesPorCurso(String curso){
        return estudanteRepository.findByCurso(curso);
    }

    public List<Estudante> listarEstudantesPorNome(String nome){
        return estudanteRepository.findByNomeContains(nome);
    }

    public List<Estudante> listarEstudantesPrimeirosEstudantes(int numero){
        return estudanteRepository.findTops(numero);
    }

}

