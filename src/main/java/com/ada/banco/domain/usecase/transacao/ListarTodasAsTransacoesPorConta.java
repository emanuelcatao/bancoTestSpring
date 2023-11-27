package com.ada.banco.domain.usecase.transacao;

import com.ada.banco.domain.gateway.ContaGateway;
import com.ada.banco.domain.gateway.TransacaoGateway;
import com.ada.banco.domain.model.Conta;
import com.ada.banco.domain.model.Transacao;

import java.util.List;

public class ListarTodasAsTransacoesPorConta {
    private final ContaGateway contaGateway;
    private final TransacaoGateway transacaoGateway;

    public ListarTodasAsTransacoesPorConta(ContaGateway contaGateway, TransacaoGateway transacaoGateway) {
        this.contaGateway = contaGateway;
        this.transacaoGateway = transacaoGateway;
    }

    public List<Transacao> execute(Long idConta) throws Exception {
        Conta conta = contaGateway.obterContaPorId(idConta);
        if (conta == null) {
            throw new Exception("Conta não encontrada.");
        }

        return transacaoGateway.listarTodasAsTransacoesDaConta(conta);
    }
}
