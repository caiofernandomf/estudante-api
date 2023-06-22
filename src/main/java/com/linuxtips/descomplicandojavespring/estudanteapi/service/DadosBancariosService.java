package com.linuxtips.descomplicandojavespring.estudanteapi.service;

import com.linuxtips.descomplicandojavespring.estudanteapi.model.DadosBancarios;
import com.linuxtips.descomplicandojavespring.estudanteapi.repository.DadosBancariosRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DadosBancariosService {

    private final DadosBancariosRepository dadosBancariosRepository;

    public ResponseEntity<DadosBancarios> atualizarDadosBancarios(Long id,DadosBancarios dadosBancarios){
       return dadosBancariosRepository.findById(id)
            .map(dadosBancarios1 -> {
                DadosBancarios dadosBancariosUpdate =
                        DadosBancarios.builder().conta(dadosBancarios.getConta())
                                .agencia(dadosBancarios.getAgencia())
                                .digito(dadosBancarios.getDigito())
                                .tipoContaBancaria(dadosBancarios.getTipoContaBancaria())
                                .id(dadosBancarios1.getId()).build();
                return
                        ResponseEntity.ok().body(dadosBancariosRepository.save(dadosBancariosUpdate));

            }).orElse(ResponseEntity.notFound().build());

    }

    public List<DadosBancarios> listarDadosBancarios(){
        return dadosBancariosRepository.findAll();
    }

}
