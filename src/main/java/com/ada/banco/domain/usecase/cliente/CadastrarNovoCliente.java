package com.ada.banco.domain.usecase.cliente;

import com.ada.banco.domain.gateway.ClienteGateway;
import com.ada.banco.domain.model.Cliente;

public class CadastrarNovoCliente {
    private final ClienteGateway clienteGateway;

    public CadastrarNovoCliente(ClienteGateway clienteGateway) {
        this.clienteGateway = clienteGateway;
    }

    public void execute(Cliente dadosCliente) {
        if(clienteGateway.buscarPorCpf(dadosCliente.getCpf()) != null){
            throw new RuntimeException("Cliente já cadastrado");
        }
        clienteGateway.salvar(dadosCliente);
    }
}
