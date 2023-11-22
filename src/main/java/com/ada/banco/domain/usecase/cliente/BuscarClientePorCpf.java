package com.ada.banco.domain.usecase.cliente;

import com.ada.banco.domain.gateway.ClienteGateway;
import com.ada.banco.domain.model.Cliente;

import java.util.Optional;

public class BuscarClientePorCpf {

    private final ClienteGateway clienteGateway;

    public BuscarClientePorCpf(ClienteGateway clienteGateway) {
        this.clienteGateway = clienteGateway;
    }

    public Cliente execute(String cpf) {
        return clienteGateway.buscarPorCpf(cpf);
    }
}
