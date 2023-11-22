package com.ada.banco.domain.gateway;

import com.ada.banco.domain.model.Cliente;
import com.ada.banco.domain.model.Conta;

import java.util.List;

public interface ClienteGateway {
    Cliente salvar(Cliente cliente);

    Cliente buscarPorCpf(String cpf);

    Cliente buscarPorId(Long id);

    List<Cliente> listarTodosOsClientes();
}