package br.com.trainer.api.controller;

import br.com.trainer.api.dto.AgendamentoDTO;
import br.com.trainer.api.entity.Agendamento;
import br.com.trainer.api.service.AgendamentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    public AgendamentoController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    @PostMapping
    public ResponseEntity<Agendamento> agendarAula(@RequestBody AgendamentoDTO dto) {
        Agendamento agendamento = agendamentoService.criarAgendamento(dto);
        return ResponseEntity.ok(agendamento);
    }

    @GetMapping
    public ResponseEntity<List<Agendamento>> buscarAgendamentos(
            @RequestParam("start") String start,
            @RequestParam("end") String end) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            LocalDateTime startDate = LocalDateTime.parse(start, formatter);
            LocalDateTime endDate = LocalDateTime.parse(end, formatter);

            List<Agendamento> agendamentos = agendamentoService.buscarAgendamentos(startDate, endDate);
            return ResponseEntity.ok(agendamentos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/atualizar")
    public ResponseEntity<Agendamento> atualizarAgendamento(@RequestBody AgendamentoDTO dto) {
        Agendamento agendamento = agendamentoService.atualizarAgendamento(dto);
        return ResponseEntity.ok(agendamento);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAgendamento(@PathVariable Long id) {
        agendamentoService.deletarAgendamento(id);
        return ResponseEntity.noContent().build();
    }
}
