package br.com.trainer.api.repository;

import br.com.trainer.api.entity.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    Optional<Aluno> findByCpf(String alunoCpf); // Retornando Optional<Aluno>
}
