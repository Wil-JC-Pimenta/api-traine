package br.com.trainer.api.controller;

import br.com.trainer.api.dto.LoginRequest;
import br.com.trainer.api.dto.LoginResponse;
import br.com.trainer.api.dto.UserDTO;
import br.com.trainer.api.dto.UserDTO;
import br.com.trainer.api.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.authenticateUser(loginRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDTO userDto) {
        authService.registerUser(userDto);
        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }

}
