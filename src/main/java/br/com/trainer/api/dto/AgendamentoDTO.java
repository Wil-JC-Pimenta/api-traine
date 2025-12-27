package br.com.trainer.api.dto;

import java.time.LocalDateTime;

public class AgendamentoDTO {

    private Long id;
    private String alunoCpf;
    private LocalDateTime dataHora;
    private Double valor;
    private String descricao;

    public AgendamentoDTO() {
    }

    public AgendamentoDTO(
            Long id,
            String alunoCpf,
            LocalDateTime dataHora,
            Double valor,
            String descricao
    ) {
        this.id = id;
        this.alunoCpf = alunoCpf;
        this.dataHora = dataHora;
        this.valor = valor;
        this.descricao = descricao;
    }

    // getters e setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlunoCpf() {
        return alunoCpf;
    }

    public void setAlunoCpf(String alunoCpf) {
        this.alunoCpf = alunoCpf;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
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
