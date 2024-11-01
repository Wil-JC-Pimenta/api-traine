package br.com.trainer.api.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class AgendamentoDTO {
    private Long id; // Armazena o ID do agendamento
    private Long alunoId; // Armazena o ID do aluno
    private LocalDate data; // Data do agendamento
    private LocalTime hora; // Hora do agendamento
    private Double valor; // Valor da aula
    private String descricao; // Descrição do agendamento

    // Construtor completo com todos os campos
    public AgendamentoDTO(Long id, Long alunoId, LocalDate data, LocalTime hora, Double valor, String descricao) {
        this.id = id;
        this.alunoId = alunoId;
        this.data = data;
        this.hora = hora;
        this.valor = valor;
        this.descricao = descricao;
    }

    // Construtor para criação de agendamento sem ID
    public AgendamentoDTO(Long alunoId, LocalDate data, LocalTime hora, Double valor, String descricao) {
        this.alunoId = alunoId;
        this.data = data;
        this.hora = hora;
        this.valor = valor;
        this.descricao = descricao;
    }

    // Construtor vazio, caso necessário para inicializações básicas
    public AgendamentoDTO() {
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(Long alunoId) {
        this.alunoId = alunoId;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
