package com.ada.banco.domain.usecase.transacao;

import com.ada.banco.domain.gateway.ContaGateway;
import com.ada.banco.domain.gateway.TransacaoGateway;
import com.ada.banco.domain.model.Conta;
import com.ada.banco.domain.model.Transacao;
import com.ada.banco.domain.model.enums.TipoConta;
import com.ada.banco.domain.model.enums.TipoTransacao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RealizarDepositoTest {

    @Mock
    private ContaGateway contaGateway;
    @Mock
    private TransacaoGateway transacaoGateway;
    @InjectMocks
    private RealizarDeposito realizarDeposito;

    @Test
    public void deveLancarExceptionParaValorInvalido() {
        Transacao transacaoInvalida = new Transacao(1L, TipoTransacao.DEPOSITO, BigDecimal.valueOf(-100), null, 1L, null);

        Exception exception = assertThrows(
                Exception.class,
                () -> realizarDeposito.execute(transacaoInvalida)
        );

        assertEquals("Valor inválido para depósito.", exception.getMessage());
    }

    @Test
    public void deveLancarExceptionParaContaInexistente() {
        Transacao transacao = new Transacao(1L, TipoTransacao.DEPOSITO, BigDecimal.valueOf(100), null, 1L, null);
        when(contaGateway.obterContaPorId(1L)).thenReturn(null);

        Exception exception = assertThrows(
                Exception.class,
                () -> realizarDeposito.execute(transacao)
        );

        assertEquals("Conta não encontrada para depósito.", exception.getMessage());
    }

    @Test
    public void deveLancarExceptionParaTipoTransacaoInvalido() {
        Transacao transacaoInvalida = new Transacao(1L, TipoTransacao.TRANSFERENCIA, BigDecimal.valueOf(100), null, 1L, null);
        when(contaGateway.obterContaPorId(1L)).thenReturn(new Conta(1L, "12345", TipoConta.POUPANCA, BigDecimal.valueOf(200), 1L));

        Exception exception = assertThrows(
                Exception.class,
                () -> realizarDeposito.execute(transacaoInvalida)
        );

        assertEquals("Tipo de transação inválido para depósito.", exception.getMessage());
    }

    @Test
    public void deveRealizarDepositoComSucesso() throws Exception {
        Long contaId = 1L;
        BigDecimal valorDeposito = BigDecimal.valueOf(100);
        Transacao transacao = new Transacao(null, TipoTransacao.DEPOSITO, valorDeposito, null, contaId, null);
        Conta conta = new Conta(contaId, "12345", TipoConta.POUPANCA, BigDecimal.valueOf(200), 1L);

        when(contaGateway.obterContaPorId(contaId)).thenReturn(conta);
        when(contaGateway.salvar(any(Conta.class))).thenAnswer(i -> i.getArguments()[0]);

        Transacao resultado = realizarDeposito.execute(transacao);

        assertNotNull(resultado);
        assertEquals(LocalDateTime.now().withNano(0), resultado.getData().withNano(0));
        assertEquals(valorDeposito, resultado.getValor());
        verify(contaGateway).salvar(conta);
        verify(transacaoGateway).registrarTransacao(transacao);
    }
}
