package br.com.trainer.api.controller;

import br.com.trainer.api.dto.AgendamentoDTO;
import br.com.trainer.api.entity.Agendamento;
import br.com.trainer.api.service.AgendamentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    public AgendamentoController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    @PostMapping
    public ResponseEntity<AgendamentoDTO> criar(
            @RequestBody AgendamentoDTO dto
    ) {
        Agendamento agendamento = agendamentoService.criarAgendamento(dto);
        return ResponseEntity.ok(mapToDTO(agendamento));
    }

    @GetMapping
    public ResponseEntity<List<AgendamentoDTO>> buscarPorPeriodo(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end
    ) {
        List<AgendamentoDTO> lista = agendamentoService
                .buscarPorPeriodo(start, end)
                .stream()
                .map(this::mapToDTO)
                .toList();

        return ResponseEntity.ok(lista);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgendamentoDTO> atualizar(
            @PathVariable Long id,
            @RequestBody AgendamentoDTO dto
    ) {
        dto.setId(id);
        Agendamento agendamento = agendamentoService.atualizarAgendamento(dto);
        return ResponseEntity.ok(mapToDTO(agendamento));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        agendamentoService.deletarAgendamento(id);
        return ResponseEntity.noContent().build();
    }

    // ===== Mapper simples (pode virar classe depois) =====
    private AgendamentoDTO mapToDTO(Agendamento agendamento) {
        AgendamentoDTO dto = new AgendamentoDTO();
        dto.setId(agendamento.getId());
        dto.setAlunoCpf(agendamento.getAluno().getCpf());
        dto.setDataHora(agendamento.getDataHora());
        dto.setValor(agendamento.getValor());
        dto.setDescricao(agendamento.getDescricao());
        return dto;
    }
}
