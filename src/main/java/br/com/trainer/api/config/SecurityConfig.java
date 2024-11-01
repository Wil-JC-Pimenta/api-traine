package br.com.trainer.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desativa o CSRF (se necessário para APIs)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/agendamentos/**").authenticated() // Exige autenticação nas rotas de agendamentos
                        .anyRequest().permitAll() // Permite acesso para outras rotas
                )
                .httpBasic(Customizer.withDefaults()); // Usa autenticação HTTP Basic

        return http.build();
    }
}
