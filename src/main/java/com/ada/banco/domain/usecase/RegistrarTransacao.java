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

    /**
     * Registra uma transação. Aqui as validações são feitas antes de registrar a transação. De modo que
     * não existe a necessidade de fazer novas validações aqui no registrar transacao.
     *
     * Fica a cargo desse usecase apenas o registro da transação.
     * @param transacao
     * @throws InvalidTransactionException
     */
    public void execute(Transacao transacao) throws InvalidTransactionException {
        if (!TipoTransacao.DEPOSITO.equals(transacao.getTipo()) &&
                !TipoTransacao.SAQUE.equals(transacao.getTipo()) &&
                !TipoTransacao.TRANSFERENCIA.equals(transacao.getTipo())
            ) {
            throw new InvalidTransactionException("Tipo de transação inválido.");
        }

        // Registra a transação após todas as validações
        transacaoGateway.registrarTransacao(transacao);
    }
}