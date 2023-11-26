package com.ada.banco.domain.gateway;

import com.ada.banco.domain.model.Conta;

import java.util.List;

public interface ContaGateway {
    Conta buscarPorCpf(String cpf);
    Conta salvar(Conta conta);
    Conta obterContaPorId(Long idConta);

    Conta obterContaPorIdCliente(Long id);

    List<Conta> listarTodas();

    Conta obterContaPorNumero(String numeroConta);
}
