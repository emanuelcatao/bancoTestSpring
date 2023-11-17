package com.ada.banco.dummy;

import com.ada.banco.domain.gateway.TransacaoGateway;
import com.ada.banco.domain.model.Transacao;

public class TransacaoGatewayDummyImpl implements TransacaoGateway {
    @Override
    public void registrarTransacao(Transacao transacao) {
        System.out.println("Registrando transação: " + transacao.getIdTransacao().toString() + " no valor de " + transacao.getValor() + " para a Conta Id: " + transacao.getIdConta().toString());
    }
}
