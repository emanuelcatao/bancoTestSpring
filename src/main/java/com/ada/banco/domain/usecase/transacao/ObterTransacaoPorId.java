package com.ada.banco.domain.usecase.transacao;

import com.ada.banco.domain.gateway.ContaGateway;
import com.ada.banco.domain.gateway.TransacaoGateway;
import com.ada.banco.domain.model.Transacao;

public class ObterTransacaoPorId {
    private final TransacaoGateway transacaoGateway;

    public ObterTransacaoPorId(TransacaoGateway transacaoGateway) {
        this.transacaoGateway = transacaoGateway;
    }

    public Transacao execute(Long IdTransacao) {
        return transacaoGateway.obterTransacaoPorId(IdTransacao);
    }
}
