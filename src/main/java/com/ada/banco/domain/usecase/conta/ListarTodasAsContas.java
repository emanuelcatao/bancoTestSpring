package com.ada.banco.domain.usecase.conta;

import com.ada.banco.domain.gateway.ContaGateway;
import com.ada.banco.domain.model.Conta;

import java.util.List;

public class ListarTodasAsContas {
    private final ContaGateway contaGateway;

    public ListarTodasAsContas(ContaGateway contaGateway) {
        this.contaGateway = contaGateway;
    }

    public List<Conta> execute() {
        return contaGateway.listarTodas();
    }
}
