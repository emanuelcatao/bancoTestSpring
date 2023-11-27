package com.ada.banco.domain.usecase.transacao;

import com.ada.banco.domain.gateway.ContaGateway;
import com.ada.banco.domain.gateway.TransacaoGateway;
import com.ada.banco.domain.model.Conta;
import com.ada.banco.domain.model.enums.TipoTransacao;
import com.ada.banco.domain.model.Transacao;

import java.math.BigDecimal;

public class RealizarTransferencia {
    private final ContaGateway contaGateway;
    private final TransacaoGateway transacaoGateway;

    public RealizarTransferencia(ContaGateway contaGateway, TransacaoGateway transacaoGateway) {
        this.contaGateway = contaGateway;
        this.transacaoGateway = transacaoGateway;
    }

    public Transacao execute(Transacao transacao) throws Exception {
        Conta contaOrigem = contaGateway.obterContaPorId(transacao.getContaOrigemId());
        Conta contaDestino = contaGateway.obterContaPorId(transacao.getContaDestinoId());
        if (transacao.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new Exception("Valor inválido para transferência.");
        }
        if (contaOrigem == null) {
            throw new Exception("Conta de origem não encontrada para transferência.");
        }
        if (contaDestino == null) {
            throw new Exception("Conta de destino não encontrada para transferência.");
        }
        if (contaOrigem.getSaldo().compareTo(transacao.getValor()) < 0) {
            throw new Exception("Saldo insuficiente para transferência.");
        }
        if (contaOrigem.getId().equals(contaDestino.getId())) {
            throw new Exception("Conta de origem e destino não podem ser iguais.");
        }
        if (!TipoTransacao.TRANSFERENCIA.equals(transacao.getTipo())) {
            throw new Exception("Tipo de transação inválido para transferência.");
        }

        BigDecimal novoSaldoOrigem = contaOrigem.getSaldo().subtract(transacao.getValor());
        BigDecimal novoSaldoDestino = contaDestino.getSaldo().add(transacao.getValor());
        contaOrigem.setSaldo(novoSaldoOrigem);
        contaDestino.setSaldo(novoSaldoDestino);
        contaGateway.salvar(contaOrigem);
        contaGateway.salvar(contaDestino);

        transacao.setData(java.time.LocalDateTime.now());
        transacaoGateway.registrarTransacao(transacao);

        return transacao;
    }
}
