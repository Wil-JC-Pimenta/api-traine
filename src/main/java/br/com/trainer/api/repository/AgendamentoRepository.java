package br.com.trainer.api.repository;

import br.com.trainer.api.entity.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    List<Agendamento> findByDataHoraBetween(LocalDateTime start, LocalDateTime end);
}
