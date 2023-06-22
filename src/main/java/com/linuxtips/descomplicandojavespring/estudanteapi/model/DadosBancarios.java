package com.linuxtips.descomplicandojavespring.estudanteapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "dados_bancarios")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class DadosBancarios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer agencia;

    @Column(nullable = false,unique = true)
    private Integer conta;

    @Column(nullable = false)
    private Integer digito;

    @Column(nullable = false)
    private TipoContaBancaria tipoContaBancaria;

    @OneToOne(mappedBy = "dadosBancarios")
    @JsonBackReference
    private Estudante estudante;
}
