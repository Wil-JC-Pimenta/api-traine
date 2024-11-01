package br.com.trainer.api.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    @ManyToOne
    @JoinColumn(name = "aluno_cpf", referencedColumnName = "cpf", nullable = false)
    private Aluno aluno;

    @Column(nullable = false)
    private Double valor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAgendamento status;

    private String descricao;
}
