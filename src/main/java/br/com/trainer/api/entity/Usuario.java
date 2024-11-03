package br.com.trainer.api.entity;

<<<<<<< HEAD
public class Usuario {
=======

import jakarta.persistence.*;

@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;

    // Construtor padrão (necessário para JPA)
    public Usuario() {
    }

    // Construtor com parâmetros
    public Usuario(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    // Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Método toString (opcional, mas útil para debugging)
    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
>>>>>>> c43ab81ec6747deba4e186f4426d6bcc662b0c94
}
