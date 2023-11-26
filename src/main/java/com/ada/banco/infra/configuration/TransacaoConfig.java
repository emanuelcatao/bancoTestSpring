package com.ada.banco.infra.configuration;

import com.ada.banco.domain.gateway.ContaGateway;
import com.ada.banco.domain.gateway.TransacaoGateway;
import com.ada.banco.domain.usecase.transacao.RealizarDeposito;
import com.ada.banco.domain.usecase.transacao.RealizarSaque;
import com.ada.banco.domain.usecase.transacao.RealizarTransferencia;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransacaoConfig {

    @Bean
    public RealizarDeposito realizarDeposito(ContaGateway contaGateway, TransacaoGateway transacaoGateway) {
        return new RealizarDeposito(contaGateway, transacaoGateway);
    }

    @Bean
    public RealizarSaque realizarSaque(ContaGateway contaGateway, TransacaoGateway transacaoGateway) {
        return new RealizarSaque(contaGateway, transacaoGateway);
    }

    @Bean
    public RealizarTransferencia realizarTransferencia(ContaGateway contaGateway, TransacaoGateway transacaoGateway) {
        return new RealizarTransferencia(contaGateway, transacaoGateway);
    }
}
