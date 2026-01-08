package br.com.trainer.api.service;

import br.com.trainer.api.dto.LoginRequest;
import br.com.trainer.api.dto.LoginResponse;
import br.com.trainer.api.dto.UserDTO;
import br.com.trainer.api.entity.Usuario;
import br.com.trainer.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import br.com.trainer.api.security.JwtTokenProvider;



@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // Método de autenticação
    public LoginResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        String token = jwtTokenProvider.createToken(authentication.getName());
        return new LoginResponse(token);
    }

    // Método de registro
    public void registerUser(UserDTO userDto) {
        if (usuarioRepository.existsByUsername(userDto.getUsername())) {
            throw new RuntimeException("Erro: Username já está em uso!");
        }

        if (usuarioRepository.existsByEmail(userDto.getEmail())) {
            throw new RuntimeException("Erro: Email já está em uso!");
        }

        // Criptografa a senha
        Usuario user = new Usuario(userDto.getUsername(),
                passwordEncoder.encode(userDto.getPassword()),
                userDto.getEmail());

        usuarioRepository.save(user);
    }
}
