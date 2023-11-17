package com.ada.banco.domain.gateway;

import com.ada.banco.domain.model.Transacao;

public interface TransacaoGateway {
    void registrarTransacao(Transacao transacao);
}