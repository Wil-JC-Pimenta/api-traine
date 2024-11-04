package br.com.trainer.api.dto;

public class AlunoDTO {
    private String cpf; // CPF do aluno
    private String nome; // Nome do aluno
    private String email; // Email do aluno

    // Construtores
    public AlunoDTO() {
    }

    public AlunoDTO(String cpf, String nome, String email) {
        this.cpf = cpf;
        this.nome = nome;
        this.email = email;
    }

    // Getters e Setters
    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
