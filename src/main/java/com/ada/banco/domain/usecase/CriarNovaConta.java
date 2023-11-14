package com.ada.banco.domain.usecase;

import com.ada.banco.domain.gateway.ContaGateway;
import com.ada.banco.domain.model.Conta;

public class CriarNovaConta {
    private final ContaGateway contaGateway;

    public CriarNovaConta(ContaGateway contaGateway) {
        this.contaGateway = contaGateway;
    }
    public Conta execute(Conta conta) throws Exception {
        // validar se o usuario ja possui uma conta
        if(contaGateway.buscarPorCpf(conta.getCpf()) != null) {
            throw new Exception("Usuário já possui uma conta");
        }
        // - se possuir vamos lançar uma exception
        // - se não possuir vamos criar uma nova conta
        return contaGateway.salvar(conta);
    }
}
