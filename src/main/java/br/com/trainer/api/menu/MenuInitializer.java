package br.com.trainer.api.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class MenuInitializer implements CommandLineRunner {

    private final AgendaMenu agendaMenu;

    @Autowired
    public MenuInitializer(AgendaMenu agendaMenu) {
        this.agendaMenu = agendaMenu;
    }

    @Override
    public void run(String... args) {
        agendaMenu.mostrarMenu();
    }
}
