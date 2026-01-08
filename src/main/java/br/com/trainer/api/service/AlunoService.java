package br.com.trainer.api.service;

import br.com.trainer.api.entity.Aluno;
import br.com.trainer.api.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import org.springframework.lang.NonNull;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;

    public Aluno criarAluno(Aluno aluno) {
        java.util.Objects.requireNonNull(aluno, "Aluno é obrigatório");
        return alunoRepository.save(aluno);
    }

    public List<Aluno> listarTodos() {
        return alunoRepository.findAll();
    }

    public Optional<Aluno> buscarPorId(@NonNull Long id) {
        return alunoRepository.findById(id);
    }

    public Aluno atualizarAluno(@NonNull Long id, Aluno alunoAtualizado) {
        java.util.Objects.requireNonNull(id, "Aluno id é obrigatório");

        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        aluno.setNome(alunoAtualizado.getNome());
        aluno.setEmail(alunoAtualizado.getEmail());
        aluno.setCpf(alunoAtualizado.getCpf());

        return alunoRepository.save(aluno);
    }

    public void deletarAluno(@NonNull Long id) {
        java.util.Objects.requireNonNull(id, "Aluno id é obrigatório");
        alunoRepository.deleteById(id);
    }
}
