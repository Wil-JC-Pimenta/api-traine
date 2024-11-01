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

    // Se você precisar de um identificador diferente do CPF, pode adicionar um campo ID
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Não é necessário implementar um getter para o ID, pois o @Data do Lombok faz isso automaticamente.
}
