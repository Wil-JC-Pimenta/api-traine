package br.com.trainer.api.repository;

<<<<<<< HEAD
public interface UsuarioRepository {
=======
import br.com.trainer.api.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
>>>>>>> c43ab81ec6747deba4e186f4426d6bcc662b0c94
}
