package com.ada.banco.infra.configuration;

import com.ada.banco.domain.gateway.ClienteGateway;
import com.ada.banco.domain.usecase.cliente.BuscarClientePorCpf;
import com.ada.banco.domain.usecase.cliente.CadastrarNovoCliente;
import com.ada.banco.domain.usecase.cliente.ListarTodosOsClientes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClienteConfig {

    @Bean
    public BuscarClientePorCpf buscarClientePorCpf(ClienteGateway clienteGateway) {
        return new BuscarClientePorCpf(clienteGateway);
    }

    @Bean
    public CadastrarNovoCliente cadastrarNovoCliente(ClienteGateway clienteGateway) {
        return new CadastrarNovoCliente(clienteGateway);
    }

    @Bean
    public ListarTodosOsClientes listarTodosOsClientes(ClienteGateway clienteGateway) {
        return new ListarTodosOsClientes(clienteGateway);
    }
}