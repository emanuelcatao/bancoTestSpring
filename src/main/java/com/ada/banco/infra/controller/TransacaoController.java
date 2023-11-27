package com.ada.banco.infra.controller;

import com.ada.banco.domain.model.Transacao;
import com.ada.banco.domain.usecase.transacao.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transacao")
public class TransacaoController {
    private final RealizarDeposito realizarDeposito;
    private final RealizarSaque realizarSaque;
    private final RealizarTransferencia realizarTransferencia;
    private final ListarTodasAsTransacoesPorConta listarTodasAsTransacoesPorConta;
    private final ObterTransacaoPorId obterTransacaoPorId;

    public TransacaoController(RealizarDeposito realizarDeposito,
                               RealizarSaque realizarSaque,
                               RealizarTransferencia realizarTransferencia,
                               ListarTodasAsTransacoesPorConta listarTodasAsTransacoesPorConta,
                               ObterTransacaoPorId obterTransacaoPorId)
    {
        this.realizarDeposito = realizarDeposito;
        this.realizarSaque = realizarSaque;
        this.realizarTransferencia = realizarTransferencia;
        this.listarTodasAsTransacoesPorConta = listarTodasAsTransacoesPorConta;
        this.obterTransacaoPorId = obterTransacaoPorId;
    }

    @PostMapping("/deposito")
    public ResponseEntity<?> realizarDeposito(@RequestBody Transacao transacao) {
        try {
            realizarDeposito.execute(transacao);
            return ResponseEntity.ok("Depósito realizado com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/saque")
    public ResponseEntity<?> realizarSaque(@RequestBody Transacao transacao) {
        try {
            realizarSaque.execute(transacao);
            return ResponseEntity.ok("Saque realizado com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/transferencia")
    public ResponseEntity<?> realizarTransferencia(@RequestBody Transacao transacao) {
        try {
            realizarTransferencia.execute(transacao);
            return ResponseEntity.ok("Transferência realizada com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/conta/{contaId}")
    public ResponseEntity<?> listarTransacoes(@PathVariable Long contaId) {
        try {
            List<Transacao> transacoes = listarTodasAsTransacoesPorConta.execute(contaId);
            return ResponseEntity.ok(transacoes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obterTransacao(@PathVariable Long id) {
        try {
            Transacao transacao = obterTransacaoPorId.execute(id);
            return ResponseEntity.ok(transacao);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
