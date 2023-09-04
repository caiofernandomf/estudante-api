package com.linuxtips.descomplicandojavespring.estudanteapi.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Estudante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private String nome;

    @Column(nullable = false,length = 10)
    private String endereco;

    @JoinColumn(name = "dados_bancarios_id",referencedColumnName = "id")
    @OneToOne(cascade = CascadeType.ALL)
    @JsonManagedReference
    private DadosBancarios dadosBancarios;

    @Column(nullable = false)
    private String curso;

    @CreationTimestamp
    @Column(updatable = false)//columnDefinition = "default 'CURRENT_TIMESTAMP' "
    private LocalDateTime criadoEm;

    @UpdateTimestamp
    @Column(updatable = true)
    private LocalDateTime atualizadoEm;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Estudante estudante)) return false;
        return Objects.equals(id, estudante.id) && Objects.equals(nome, estudante.nome) && Objects.equals(endereco, estudante.endereco) && Objects.equals(dadosBancarios, estudante.dadosBancarios) && Objects.equals(curso, estudante.curso) && Objects.equals(criadoEm, estudante.criadoEm) && Objects.equals(atualizadoEm, estudante.atualizadoEm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, endereco, dadosBancarios, curso, criadoEm, atualizadoEm);
    }
}
