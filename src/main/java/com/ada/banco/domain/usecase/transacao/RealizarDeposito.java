package com.ada.banco.domain.usecase.transacao;

import com.ada.banco.domain.gateway.ContaGateway;
import com.ada.banco.domain.gateway.TransacaoGateway;
import com.ada.banco.domain.model.Conta;
import com.ada.banco.domain.model.enums.TipoTransacao;
import com.ada.banco.domain.model.Transacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RealizarDeposito {
    private final ContaGateway contaGateway;
    private final TransacaoGateway transacaoGateway;

    public RealizarDeposito(ContaGateway contaGateway, TransacaoGateway transacaoGateway) {
        this.contaGateway = contaGateway;
        this.transacaoGateway = transacaoGateway;
    }

    public Transacao execute(Transacao transacao) throws Exception {
        Conta conta = contaGateway.obterContaPorId(transacao.getContaOrigemId());
        if (transacao.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new Exception("Valor inválido para depósito.");
        }
        if (conta == null) {
            throw new Exception("Conta não encontrada para depósito.");
        }
        if (!TipoTransacao.DEPOSITO.equals(transacao.getTipo())) {
            throw new Exception("Tipo de transação inválido para depósito.");
        }

        BigDecimal saldoAtual = conta.getSaldo();
        BigDecimal novoSaldo = saldoAtual.add(transacao.getValor());
        conta.setSaldo(novoSaldo);
        contaGateway.salvar(conta);
        transacao.setData(LocalDateTime.now());
        transacaoGateway.registrarTransacao(transacao);

        return transacao;
    }
}
