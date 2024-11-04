package br.com.trainer.api.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
public class Aluno {

    @Id
    private String cpf; // CPF como chave primária

    private String nome;
    private String email;

    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Agendamento> agendamentos;
}
