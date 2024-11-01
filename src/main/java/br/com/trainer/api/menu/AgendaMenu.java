package br.com.trainer.api.menu;

import br.com.trainer.api.dto.AgendamentoDTO;
import br.com.trainer.api.service.AgendamentoService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class AgendaMenu {
    private final AgendamentoService agendamentoService; // Serviço para manipular agendamentos
    private final Scanner scanner; // Scanner para entrada do usuário

    public AgendaMenu(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
        this.scanner = new Scanner(System.in);
    }

    public void exibirMenu() {
        while (true) {
            System.out.println("=== Menu de Agendamento ===");
            System.out.println("1. Adicionar Agendamento");
            System.out.println("2. Listar Agendamentos");
            System.out.println("3. Sair");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir a nova linha

            switch (opcao) {
                case 1:
                    adicionarAgendamento();
                    break;
                case 2:
                    listarAgendamentos();
                    break;
                case 3:
                    System.out.println("Saindo do menu...");
                    return; // Sai do método e do menu
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private void adicionarAgendamento() {

    }

    private void listarAgendamentos() {
        // Implementar lógica para listar agendamentos
        // Exemplo: Listar todos os agendamentos existentes no serviço
        System.out.println("Listando agendamentos...");
        // Aqui você chamaria um método do AgendamentoService para obter e exibir os agendamentos
    }

    public void mostrarMenu() {
    }
}
