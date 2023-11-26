package com.ada.banco.domain.usecase.utils;

import com.ada.banco.domain.gateway.ContaGateway;

import java.util.UUID;

public class GerarNumeroDeContaUnico {
    private ContaGateway contaGateway;

    public GerarNumeroDeContaUnico(ContaGateway contaGateway) {
        this.contaGateway = contaGateway;
    }

    public String execute() {
        String numeroConta;
        do {
            numeroConta = UUID.randomUUID().toString().replaceAll("-", "").substring(0,6);
        } while (contaGateway.obterContaPorNumero(numeroConta) != null);
        return numeroConta;
    }
}
