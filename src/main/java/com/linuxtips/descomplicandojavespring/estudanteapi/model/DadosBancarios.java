package com.linuxtips.descomplicandojavespring.estudanteapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

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

    @Override
    public String toString(){
        StringBuilder retorno= new StringBuilder();
        retorno.append("[id=").append(this.id).append(",");
        retorno.append("agencia=").append(this.agencia).append(",");
        retorno.append("conta=").append(this.conta).append(",");
        retorno.append("digito=").append(this.digito).append(",");
        retorno.append("tipoContaBancaria=").append(this.tipoContaBancaria).append("]");

        return retorno.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DadosBancarios that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(agencia, that.agencia) && Objects.equals(conta, that.conta) && Objects.equals(digito, that.digito) && tipoContaBancaria == that.tipoContaBancaria;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, agencia, conta, digito, tipoContaBancaria);
    }
}
