package com.ada.banco.infra.controller;

import com.ada.banco.domain.model.Conta;
import com.ada.banco.domain.usecase.conta.CriarNovaConta;
import com.ada.banco.domain.usecase.conta.ListarTodasAsContas;
import com.ada.banco.domain.usecase.conta.ObterContaPorId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contas")
public class ContaController {

    private final CriarNovaConta criarNovaContaUseCase;
    private final ObterContaPorId obterContaPorIdUseCase;
    private final ListarTodasAsContas listarTodasAsContasUseCase;

    public ContaController(CriarNovaConta criarNovaContaUseCase,
                           ObterContaPorId obterContaPorIdUseCase,
                           ListarTodasAsContas listarTodasAsContasUseCase) {
        this.criarNovaContaUseCase = criarNovaContaUseCase;
        this.obterContaPorIdUseCase = obterContaPorIdUseCase;
        this.listarTodasAsContasUseCase = listarTodasAsContasUseCase;
    }

    @PostMapping
    public ResponseEntity<?> abrirConta(@RequestBody Conta conta) {
        try {
            Conta novaConta = criarNovaContaUseCase.execute(conta);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaConta);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao abrir conta: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obterConta(@PathVariable Long id) {
        try {
            Conta conta = obterContaPorIdUseCase.execute(id);
            return ResponseEntity.ok(conta);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Conta>> listarContas() {
        List<Conta> contas = listarTodasAsContasUseCase.execute();
        return ResponseEntity.ok(contas);
    }
}
