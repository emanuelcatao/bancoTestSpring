package com.ada.banco.domain.gateway;

import com.ada.banco.domain.model.Conta;
import com.ada.banco.domain.model.Transacao;

import java.util.List;

public interface TransacaoGateway {
    boolean registrarTransacao(Transacao transacao);

    Transacao obterTransacaoPorId(Long id);

    List<Transacao> listarTodasAsTransacoesDaConta(Conta conta);
}