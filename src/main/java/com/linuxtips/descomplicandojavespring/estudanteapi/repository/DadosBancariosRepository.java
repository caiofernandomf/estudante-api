package com.linuxtips.descomplicandojavespring.estudanteapi.repository;

import com.linuxtips.descomplicandojavespring.estudanteapi.model.DadosBancarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

public interface DadosBancariosRepository extends JpaRepository<DadosBancarios,Long> {
}
