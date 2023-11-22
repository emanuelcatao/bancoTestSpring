package com.ada.banco.domain.gateway;

import com.ada.banco.domain.model.Cliente;

public interface ClienteGateway {
    Cliente salvar(Cliente cliente);

    Cliente buscarPorCpf(String cpf);
}