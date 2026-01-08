package br.com.trainer.api.service;

import br.com.trainer.api.dto.AgendamentoDTO;
import br.com.trainer.api.entity.Agendamento;
import br.com.trainer.api.entity.Aluno;
import br.com.trainer.api.repository.AgendamentoRepository;
import br.com.trainer.api.repository.AlunoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import org.springframework.lang.NonNull;

@Service
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final AlunoRepository alunoRepository;

    public AgendamentoService(
            AgendamentoRepository agendamentoRepository,
            AlunoRepository alunoRepository
    ) {
        this.agendamentoRepository = agendamentoRepository;
        this.alunoRepository = alunoRepository;
    }

    public Agendamento criarAgendamento(AgendamentoDTO dto) {

        Aluno aluno = alunoRepository.findByCpf(dto.getAlunoCpf())
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        Agendamento agendamento = new Agendamento();
        agendamento.setAluno(aluno);
        agendamento.setDataHora(dto.getDataHora());
        agendamento.setValor(dto.getValor());
        agendamento.setDescricao(dto.getDescricao());

        return agendamentoRepository.save(agendamento);
    }

    public List<Agendamento> buscarPorPeriodo(
            LocalDateTime inicio,
            LocalDateTime fim
    ) {
        return agendamentoRepository.findByDataHoraBetween(inicio, fim);
    }

    public List<Agendamento> buscarTodos() {
        return agendamentoRepository.findAll();
    }

    public Agendamento atualizarAgendamento(AgendamentoDTO dto) {

    Long id = Objects.requireNonNull(dto.getId(), "Agendamento id é obrigatório");

    Agendamento agendamento = agendamentoRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));

        Aluno aluno = alunoRepository.findByCpf(dto.getAlunoCpf())
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        agendamento.setAluno(aluno);
        agendamento.setDataHora(dto.getDataHora());
        agendamento.setValor(dto.getValor());
        agendamento.setDescricao(dto.getDescricao());

        return agendamentoRepository.save(agendamento);
    }

    public void deletarAgendamento(@NonNull Long id) {
        java.util.Objects.requireNonNull(id, "Agendamento id é obrigatório");
        agendamentoRepository.deleteById(id);
    }
}
