package com.linuxtips.descomplicandojavespring.estudanteapi.controller;

import com.linuxtips.descomplicandojavespring.estudanteapi.model.DadosBancarios;
import com.linuxtips.descomplicandojavespring.estudanteapi.service.DadosBancariosService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v2")
@AllArgsConstructor
public class DadosBancariosController {

    private final DadosBancariosService dadosBancariosService;

    @GetMapping("/listar")
    @ResponseStatus(HttpStatus.OK)
    public List<DadosBancarios> listar(){
        return dadosBancariosService.listarDadosBancarios();
    }

    @PutMapping("/atualizar/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<DadosBancarios> atualizar(@RequestBody DadosBancarios dadosBancarios, @PathVariable Long id){
        return dadosBancariosService.atualizarDadosBancarios(id,dadosBancarios);
    }
}
