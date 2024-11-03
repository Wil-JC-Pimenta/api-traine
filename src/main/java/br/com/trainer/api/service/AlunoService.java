package br.com.trainer.api.service;

import br.com.trainer.api.entity.Aluno;
import br.com.trainer.api.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;

    public Aluno criarAluno(Aluno aluno) {
        return alunoRepository.save(aluno);
    }

    public List<Aluno> listarTodos() {
        return alunoRepository.findAll();
    }

    public Optional<Aluno> buscarPorId(Long id) {
        return alunoRepository.findById(id);
    }

    public Aluno atualizarAluno(Long id, Aluno alunoAtualizado) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        aluno.setNome(alunoAtualizado.getNome());
        aluno.setEmail(alunoAtualizado.getEmail());
        aluno.setCpf(alunoAtualizado.getCpf());

        return alunoRepository.save(aluno);
    }

    public void deletarAluno(Long id) {
        alunoRepository.deleteById(id);
    }
}
