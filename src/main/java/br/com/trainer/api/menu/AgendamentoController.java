package br.com.trainer.api.menu;

import br.com.trainer.api.dto.AgendamentoDTO;
import br.com.trainer.api.entity.Agendamento;
import br.com.trainer.api.service.AgendamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AgendamentoController {

    @Autowired
    private AgendamentoService agendamentoService; // Injeção de dependência do AgendamentoService

    @PostMapping("/agendamentos") // É uma boa prática definir o caminho
    public ResponseEntity<Agendamento> adicionarAgendamento(@RequestBody AgendamentoDTO dto) {
        try {
            // Cria o agendamento com os dados recebidos
            Agendamento agendamento = agendamentoService.criarAgendamento(dto);

            if (agendamento != null) {
                return ResponseEntity.ok(agendamento); // Retorna o agendamento criado
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(null); // Retorna erro se não conseguiu criar
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null); // Retorna erro caso ocorra uma exceção
        }
    }
}
