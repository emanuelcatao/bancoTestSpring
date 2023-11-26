package com.ada.banco.domain.usecase.conta;

import com.ada.banco.domain.gateway.ClienteGateway;
import com.ada.banco.domain.gateway.ContaGateway;
import com.ada.banco.domain.model.Conta;

public class ObterContaPorId {
    private final ContaGateway contaGateway;

    public ObterContaPorId(ContaGateway contaGateway) {
        this.contaGateway = contaGateway;
    }

    public Conta execute(Long idConta) {
        Conta conta = contaGateway.obterContaPorId(idConta);
        if(conta == null) {
            throw new RuntimeException("Conta não encontrada");
        }
        return conta;
    }
}
