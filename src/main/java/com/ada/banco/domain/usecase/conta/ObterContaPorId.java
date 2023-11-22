package com.ada.banco.domain.usecase.conta;

import com.ada.banco.domain.gateway.ClienteGateway;
import com.ada.banco.domain.gateway.ContaGateway;
import com.ada.banco.domain.model.Conta;

public class ObterContaPorId {
    private ContaGateway contaGateway;

    public ObterContaPorId(ContaGateway contaGateway) {
        this.contaGateway = contaGateway;
    }

    public Conta execute(Long idConta) {
        return contaGateway.obterContaPorId(idConta);
    }
}
