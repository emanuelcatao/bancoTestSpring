package com.ada.banco.domain.usecase.transacao;

import com.ada.banco.domain.gateway.ContaGateway;
import com.ada.banco.domain.gateway.TransacaoGateway;
import com.ada.banco.domain.model.Conta;
import com.ada.banco.domain.model.TipoTransacao;
import com.ada.banco.domain.model.Transacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RealizarSaque {
    private final ContaGateway contaGateway;
    private final TransacaoGateway transacaoGateway;

    public RealizarSaque(ContaGateway contaGateway, TransacaoGateway transacaoGateway) {
        this.contaGateway = contaGateway;
        this.transacaoGateway = transacaoGateway;
    }

    public void execute(Transacao transacao) throws Exception {
        Conta conta = contaGateway.obterContaPorId(transacao.getContaOrigemId());
        if (transacao.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new Exception("Valor inválido para saque.");
        }
        if (conta == null) {
            throw new Exception("Conta não encontrada para saque.");
        }
        if (contaGateway.obterContaPorId(transacao.getContaDestinoId())
                .getSaldo().compareTo(transacao.getValor()) < 0) {
            throw new Exception("Saldo insuficiente para saque.");
        }
        if (!TipoTransacao.SAQUE.equals(transacao.getTipo())) {
            throw new Exception("Tipo de transação inválido para saque.");
        }

        BigDecimal novoSaldo = contaGateway.obterContaPorId(
                transacao.getContaDestinoId()).getSaldo()
                .subtract(transacao.getValor()
                );
        conta.setSaldo(novoSaldo);
        transacao.setData(LocalDateTime.now());
        contaGateway.salvar(conta);
        transacaoGateway.registrarTransacao(transacao);
    }
}
