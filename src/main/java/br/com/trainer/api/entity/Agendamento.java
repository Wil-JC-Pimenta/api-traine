package br.com.trainer.api.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Data
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID do agendamento

    @ManyToOne
    @JoinColumn(name = "aluno_cpf", nullable = false)
    private Aluno aluno; // Referência ao aluno

    private LocalDate data; // Data do agendamento
    private LocalTime hora; // Hora do agendamento
    private Double valor; // Valor da aula
    private String descricao; // Descrição do agendamento

    // Método para obter a data e hora como LocalDateTime
    public LocalDateTime getDataHora() {
        return LocalDateTime.of(data, hora);
    }

    // Método para configurar data e hora a partir de LocalDateTime
    public void setDataHora(LocalDateTime dataHora) {
        this.data = dataHora.toLocalDate();
        this.hora = dataHora.toLocalTime();
    }
}
