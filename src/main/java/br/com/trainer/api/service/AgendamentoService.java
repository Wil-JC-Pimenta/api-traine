package br.com.trainer.api.service;

import br.com.trainer.api.dto.AgendamentoDTO;
import br.com.trainer.api.entity.Agendamento;
import br.com.trainer.api.repository.AgendamentoRepository;
import br.com.trainer.api.entity.Aluno;
import br.com.trainer.api.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AgendamentoService {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    public Agendamento criarAgendamento(AgendamentoDTO dto) {
        Aluno aluno = alunoRepository.findById(dto.getAlunoId())
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado")); // Usando alunoId em vez de alunoCpf

        Agendamento agendamento = new Agendamento();
        agendamento.setAluno(aluno);
        agendamento.setDataHora(LocalDateTime.of(dto.getData(), dto.getHora()));
        agendamento.setValor(dto.getValor());
        agendamento.setDescricao(dto.getDescricao());

        return agendamentoRepository.save(agendamento);
    }

    public List<Agendamento> buscarAgendamentos(LocalDateTime start, LocalDateTime end) {
        return agendamentoRepository.findByDataHoraBetween(start, end);
    }

    public List<Agendamento> buscarTodos() {
        return agendamentoRepository.findAll();
    }

    public Agendamento atualizarAgendamento(AgendamentoDTO dto) {
        Agendamento agendamento = agendamentoRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));

        Aluno aluno = alunoRepository.findById(dto.getAlunoId()) // Usando alunoId em vez de alunoCpf
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        agendamento.setAluno(aluno);
        agendamento.setDataHora(LocalDateTime.of(dto.getData(), dto.getHora()));
        agendamento.setValor(dto.getValor());
        agendamento.setDescricao(dto.getDescricao());

        return agendamentoRepository.save(agendamento);
    }

    public void deletarAgendamento(Long id) {
        agendamentoRepository.deleteById(id);
    }
}
