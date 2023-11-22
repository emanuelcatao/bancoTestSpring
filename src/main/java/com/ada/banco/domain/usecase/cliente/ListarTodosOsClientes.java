package com.ada.banco.domain.usecase.cliente;

import com.ada.banco.domain.gateway.ClienteGateway;
import com.ada.banco.domain.gateway.ContaGateway;
import com.ada.banco.domain.model.Cliente;
import com.ada.banco.domain.model.Conta;

import java.util.List;

public class ListarTodosOsClientes {
    private final ClienteGateway clienteGateway;

    public ListarTodosOsClientes(ClienteGateway clienteGateway) {
        this.clienteGateway = clienteGateway;
    }

    public List<Cliente> execute() {
        return clienteGateway.listarTodosOsClientes();
    }
}
