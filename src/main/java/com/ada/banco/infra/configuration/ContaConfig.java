package com.ada.banco.infra.configuration;

import com.ada.banco.domain.gateway.ClienteGateway;
import com.ada.banco.domain.gateway.ContaGateway;
import com.ada.banco.domain.usecase.conta.CriarNovaConta;
import com.ada.banco.domain.usecase.conta.ListarTodasAsContas;
import com.ada.banco.domain.usecase.conta.ObterContaPorId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContaConfig {

    @Bean
    public CriarNovaConta criarNovaConta(ContaGateway contaGateway, ClienteGateway clienteGateway) {
        return new CriarNovaConta(contaGateway, clienteGateway);
    }

    @Bean
    public ListarTodasAsContas listarTodasAsContas(ContaGateway contaGateway) {
        return new ListarTodasAsContas(contaGateway);
    }

    @Bean
    public ObterContaPorId obterContaPorId(ContaGateway contaGateway) {
        return new ObterContaPorId(contaGateway);
    }
}