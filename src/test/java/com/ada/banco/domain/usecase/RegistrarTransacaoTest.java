package com.ada.banco.domain.usecase;

import com.ada.banco.domain.gateway.ContaGateway;
import com.ada.banco.domain.gateway.TransacaoGateway;
import com.ada.banco.domain.model.TipoTransacao;
import com.ada.banco.domain.model.Transacao;
import com.ada.banco.domain.usecase.exception.InvalidTransactionException;
import com.ada.banco.dummy.ContaGatewayDummyImpl;
import com.ada.banco.dummy.TransacaoGatewayDummyImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RegistrarTransacaoTest {

    private RegistrarTransacao registrarTransacao;

    @BeforeEach
    public void setUp() {
        TransacaoGateway transacaoGateway = new TransacaoGatewayDummyImpl();
        ContaGateway contaGateway = new ContaGatewayDummyImpl();
        registrarTransacao = new RegistrarTransacao(transacaoGateway, contaGateway);
    }

    @Test
    public void deveLancarExceptionQuandoValorInvalido() {
        Transacao transacao = new Transacao(1L, 1L, TipoTransacao.DEPOSITO, BigDecimal.valueOf(-100), LocalDateTime.now());
        Assertions.assertThrows(InvalidTransactionException.class, () -> registrarTransacao.execute(transacao));
    }

    @Test
    public void deveLancarExceptionQuandoTipoTransacaoInvalido() {
        Transacao transacao = new Transacao(1L, 1L, null, BigDecimal.valueOf(100), LocalDateTime.now());
        Assertions.assertThrows(InvalidTransactionException.class, () -> registrarTransacao.execute(transacao));
    }

    @Test
    public void deveLancarExceptionQuandoContaInexistente() {
        Transacao transacao = new Transacao(1L, 999L, TipoTransacao.DEPOSITO, BigDecimal.valueOf(100), LocalDateTime.now());
        Assertions.assertThrows(InvalidTransactionException.class, () -> registrarTransacao.execute(transacao));
    }

    @Test
    public void deveLancarExceptionQuandoSaldoInsuficienteParaSaque() {
        Transacao transacao = new Transacao(1L, 1L, TipoTransacao.SAQUE, BigDecimal.valueOf(1000), LocalDateTime.now());
        Assertions.assertThrows(InvalidTransactionException.class, () -> registrarTransacao.execute(transacao));
    }

    @Test
    public void deveRegistrarTransacaoComSucesso() throws InvalidTransactionException {
        Transacao transacao = new Transacao(1L, 1L, TipoTransacao.DEPOSITO, BigDecimal.valueOf(100), LocalDateTime.now());
        registrarTransacao.execute(transacao);
    }
}