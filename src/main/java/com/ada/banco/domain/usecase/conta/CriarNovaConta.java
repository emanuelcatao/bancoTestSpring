package com.ada.banco.domain.usecase.conta;

import com.ada.banco.domain.gateway.ClienteGateway;
import com.ada.banco.domain.gateway.ContaGateway;
import com.ada.banco.domain.model.Cliente;
import com.ada.banco.domain.model.Conta;
import com.ada.banco.domain.usecase.utils.GerarNumeroDeContaUnico;

public class CriarNovaConta {
    private final ContaGateway contaGateway;
    private final ClienteGateway clienteGateway;

    private GerarNumeroDeContaUnico gerarNumeroDeContaUnico;

    public CriarNovaConta(ContaGateway contaGateway, ClienteGateway clienteGateway, GerarNumeroDeContaUnico gerarNumeroDeContaUnico) {
        this.contaGateway = contaGateway;
        this.clienteGateway = clienteGateway;
        this.gerarNumeroDeContaUnico = gerarNumeroDeContaUnico;
    }

    public Conta execute(Conta conta) throws Exception {
        Cliente cliente = clienteGateway.buscarPorId(conta.getIdCliente());
        if (cliente == null) {
            throw new Exception("Cliente não encontrado para o Id informado.");
        }

        if(contaGateway.obterContaPorIdCliente(cliente.getId()) != null) {
            throw new Exception("Usuário já possui uma conta");
        }

        return contaGateway.salvar(conta);
    }
}
