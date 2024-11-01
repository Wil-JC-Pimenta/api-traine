package br.com.trainer.api.controller; // Mantenha o pacote original se não houver conflito

import br.com.trainer.api.dto.AgendamentoDTO;
import br.com.trainer.api.entity.Agendamento;
import br.com.trainer.api.service.AgendamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/agendamentos") // Considere alterar para /agendamentos se for mais lógico
public class AgendamentoController { // Se necessário, renomeie para MenuAgendamentoController

    @Autowired
    private AgendamentoService agendamentoService;

    @PostMapping
    public ResponseEntity<Agendamento> agendarAula(@RequestBody AgendamentoDTO dto) {
        Agendamento agendamento = agendamentoService.criarAgendamento(dto);
        return ResponseEntity.ok(agendamento);
    }

    @GetMapping
    public ResponseEntity<List<Agendamento>> buscarAgendamentos(
            @RequestParam("start") String start,
            @RequestParam("end") String end) {

        LocalDateTime startDate = LocalDateTime.parse(start);
        LocalDateTime endDate = LocalDateTime.parse(end);

        List<Agendamento> agendamentos = agendamentoService.buscarAgendamentos(startDate, endDate);
        return ResponseEntity.ok(agendamentos);
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
