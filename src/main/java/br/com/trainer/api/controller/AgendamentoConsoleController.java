package br.com.trainer.api.controller;

import br.com.trainer.api.dto.AgendamentoDTO;
import br.com.trainer.api.entity.Agendamento;
import br.com.trainer.api.entity.Aluno;
import br.com.trainer.api.repository.AlunoRepository;
import br.com.trainer.api.service.AgendamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

@Component
public class AgendamentoConsoleController {

    @Autowired
    private AgendamentoService agendamentoService;

    @Autowired
    private AlunoRepository alunoRepository;

    private Scanner scanner = new Scanner(System.in); // Inicializa o scanner

    public void adicionarAgendamento() {
        try {
            System.out.print("CPF do Aluno: ");
            String alunoCpf = scanner.nextLine(); // Captura o CPF do aluno

            // Busca o aluno pelo CPF
            Aluno aluno = alunoRepository.findByCpf(alunoCpf)
                    .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

            System.out.print("Data (dd/MM/yyyy): ");
            String dataInput = scanner.nextLine();
            LocalDate data = LocalDate.parse(dataInput, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            System.out.print("Hora (HH:mm): ");
            String horaInput = scanner.nextLine();
            LocalTime hora = LocalTime.parse(horaInput, DateTimeFormatter.ofPattern("HH:mm"));

            System.out.print("Valor da Aula: ");
            Double valor = scanner.nextDouble();
            scanner.nextLine(); // Consumir a nova linha

            System.out.print("Descrição: ");
            String descricao = scanner.nextLine();

            // Cria o DTO com o ID do aluno
            AgendamentoDTO agendamentoDTO = new AgendamentoDTO(aluno.getId(), data, hora, valor, descricao);
            Agendamento agendamento = agendamentoService.criarAgendamento(agendamentoDTO);

            if (agendamento != null) {
                System.out.println("Agendamento adicionado com sucesso! ID do Agendamento: " + agendamento.getId());
            } else {
                System.out.println("Erro ao adicionar agendamento.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao adicionar agendamento: " + e.getMessage());
        }
    }
}
