package com.ada.banco.domain.usecase;

import com.ada.banco.domain.gateway.ContaGateway;
import com.ada.banco.domain.gateway.TransacaoGateway;
import com.ada.banco.domain.model.Conta;
import com.ada.banco.domain.model.Transacao;
import com.ada.banco.domain.model.TipoTransacao;
import com.ada.banco.domain.usecase.exception.InvalidTransactionException;

import java.math.BigDecimal;

public class RegistrarTransacao {

    private final TransacaoGateway transacaoGateway;
    private final ContaGateway contaGateway;

    public RegistrarTransacao(TransacaoGateway transacaoGateway, ContaGateway contaGateway) {
        this.transacaoGateway = transacaoGateway;
        this.contaGateway = contaGateway;
    }

    public void execute(Transacao transacao) throws InvalidTransactionException {
        if (transacao.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransactionException("Valor da transação inválido.");
        }

        if (!TipoTransacao.DEPOSITO.equals(transacao.getTipo()) && !TipoTransacao.SAQUE.equals(transacao.getTipo())) {
            throw new InvalidTransactionException("Tipo de transação inválido.");
        }

        // Validação da conta
        Conta conta = contaGateway.obterContaPorId(transacao.getIdConta());
        if (conta == null ) { // || conta.isBloqueada()
            throw new InvalidTransactionException("Conta inválida.");
        }

        if ((conta.getSaldo().compareTo(transacao.getValor()) < 0) && (transacao.getTipo().equals(TipoTransacao.SAQUE))){
            throw new InvalidTransactionException("Saldo insuficiente para saque.");
        }

        // Registra a transação após todas as validações
        transacaoGateway.registrarTransacao(transacao);
    }
}