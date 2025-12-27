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

    @ManyToOne
    @JoinColumn(name = "aluno_cpf", nullable = false)
    private Aluno aluno;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    private Double valor;
    private String descricao;
}
