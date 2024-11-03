package br.com.trainer.api.controller;

import br.com.trainer.api.entity.Aluno;
import br.com.trainer.api.service.AlunoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    @Autowired
    private AlunoService alunoService;

    @PostMapping
    public ResponseEntity<Aluno> criarAluno(@RequestBody Aluno aluno) {
        Aluno novoAluno = alunoService.criarAluno(aluno);
        return new ResponseEntity<>(novoAluno, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Aluno>> listarTodos() {
        List<Aluno> alunos = alunoService.listarTodos();
        return new ResponseEntity<>(alunos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aluno> buscarPorId(@PathVariable Long id) {
        return alunoService.buscarPorId(id)
                .map(aluno -> new ResponseEntity<>(aluno, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Aluno> atualizarAluno(@PathVariable Long id, @RequestBody Aluno alunoAtualizado) {
        Aluno aluno = alunoService.atualizarAluno(id, alunoAtualizado);
        return new ResponseEntity<>(aluno, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAluno(@PathVariable Long id) {
        alunoService.deletarAluno(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
