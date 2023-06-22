package com.linuxtips.descomplicandojavespring.estudanteapi.repository;

import com.linuxtips.descomplicandojavespring.estudanteapi.model.Estudante;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstudamteRepository extends JpaRepository<Estudante,Long> {

    @Query("SELECT c from Estudante c WHERE c.nome=:nome")
    Optional<Estudante> findByName(String nome);

    @Query("SELECT c from Estudante c WHERE c.curso=:curso")
    List<Estudante> findByCurso(String curso);

    List<Estudante> findByNomeContains(String nome);

    List<Estudante> findByNomeStartsWithAndCurso(String nome,String curso);


}
