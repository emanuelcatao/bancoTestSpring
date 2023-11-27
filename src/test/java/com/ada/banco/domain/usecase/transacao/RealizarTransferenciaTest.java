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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RealizarTransferenciaTest {
    @Mock
    private ContaGateway contaGateway;
    @Mock
    private TransacaoGateway transacaoGateway;
    @InjectMocks
    private RealizarTransferencia realizarTransferencia;

    @Test
    public void deveLancarExceptionParaValorInvalido() {
        Transacao transacaoInvalida = new Transacao(1L, TipoTransacao.TRANSFERENCIA, BigDecimal.valueOf(-100), null, 1L, 2L);
        when(contaGateway.obterContaPorId(1L)).thenReturn(new Conta(1L, "12345", TipoConta.CORRENTE, BigDecimal.valueOf(200), 1L));
        when(contaGateway.obterContaPorId(2L)).thenReturn(new Conta(2L, "67890", TipoConta.CORRENTE, BigDecimal.valueOf(100), 2L));

        Exception exception = assertThrows(
                Exception.class,
                () -> realizarTransferencia.execute(transacaoInvalida)
        );

        assertEquals("Valor inválido para transferência.", exception.getMessage());

        verify(transacaoGateway, never()).registrarTransacao(any(Transacao.class));
        verify(contaGateway, never()).salvar(any(Conta.class));
    }

    @Test
    public void deveLancarExceptionParaContaOrigemInexistente() {
        Transacao transacao = new Transacao(1L, TipoTransacao.TRANSFERENCIA, BigDecimal.valueOf(100), null, 1L, 2L);
        when(contaGateway.obterContaPorId(1L)).thenReturn(null);
        when(contaGateway.obterContaPorId(2L)).thenReturn(new Conta(2L, "67890", TipoConta.CORRENTE, BigDecimal.valueOf(100), 2L));

        Exception exception = assertThrows(
                Exception.class,
                () -> realizarTransferencia.execute(transacao)
        );

        assertEquals("Conta de origem não encontrada para transferência.", exception.getMessage());

        verify(transacaoGateway, never()).registrarTransacao(any(Transacao.class));
        verify(contaGateway, never()).salvar(any(Conta.class));
    }

    @Test
    public void deveLancarExceptionParaContaDestinoInexistente() {
        Transacao transacao = new Transacao(1L, TipoTransacao.TRANSFERENCIA, BigDecimal.valueOf(100), null, 1L, 2L);
        when(contaGateway.obterContaPorId(1L)).thenReturn(new Conta(1L, "12345", TipoConta.CORRENTE, BigDecimal.valueOf(200), 1L));
        when(contaGateway.obterContaPorId(2L)).thenReturn(null);

        Exception exception = assertThrows(
                Exception.class,
                () -> realizarTransferencia.execute(transacao)
        );

        assertEquals("Conta de destino não encontrada para transferência.", exception.getMessage());

        verify(transacaoGateway, never()).registrarTransacao(any(Transacao.class));
        verify(contaGateway, never()).salvar(any(Conta.class));
    }

    @Test
    public void deveLancarExceptionParaSaldoInsuficienteNaContaOrigem() {
        Transacao transacao = new Transacao(1L, TipoTransacao.TRANSFERENCIA, BigDecimal.valueOf(100), null, 1L, 2L);
        when(contaGateway.obterContaPorId(1L)).thenReturn(new Conta(1L, "12345", TipoConta.CORRENTE, BigDecimal.valueOf(50), 1L));
        when(contaGateway.obterContaPorId(2L)).thenReturn(new Conta(2L, "67890", TipoConta.CORRENTE, BigDecimal.valueOf(100), 2L));

        Exception exception = assertThrows(
                Exception.class,
                () -> realizarTransferencia.execute(transacao)
        );

        assertEquals("Saldo insuficiente para transferência.", exception.getMessage());
        verify(transacaoGateway, never()).registrarTransacao(any(Transacao.class));
        verify(contaGateway, never()).salvar(any(Conta.class));
    }

    @Test
    public void deveLancarExceptionSeContaOrigemEIgualContaDestino() {
        Transacao transacao = new Transacao(1L, TipoTransacao.TRANSFERENCIA, BigDecimal.valueOf(100), null, 1L, 1L);
        when(contaGateway.obterContaPorId(1L)).thenReturn(new Conta(1L, "12345", TipoConta.CORRENTE, BigDecimal.valueOf(200), 1L));
        when(contaGateway.obterContaPorId(1L)).thenReturn(new Conta(1L, "12345", TipoConta.CORRENTE, BigDecimal.valueOf(200), 1L));

        Exception exception = assertThrows(
                Exception.class,
                () -> realizarTransferencia.execute(transacao)
        );

        assertEquals("Conta de origem e destino não podem ser iguais.", exception.getMessage());
        verify(transacaoGateway, never()).registrarTransacao(any(Transacao.class));
        verify(contaGateway, never()).salvar(any(Conta.class));
    }

    @Test
    public void deveLancarExceptionSeTipoTransacaoDiferenteDeTransferencia() {
        Transacao transacao = new Transacao(1L, TipoTransacao.DEPOSITO, BigDecimal.valueOf(100), null, 1L, 2L);
        when(contaGateway.obterContaPorId(1L)).thenReturn(new Conta(1L, "12345", TipoConta.CORRENTE, BigDecimal.valueOf(200), 1L));
        when(contaGateway.obterContaPorId(2L)).thenReturn(new Conta(2L, "67890", TipoConta.CORRENTE, BigDecimal.valueOf(100), 2L));

        Exception exception = assertThrows(
                Exception.class,
                () -> realizarTransferencia.execute(transacao)
        );

        assertEquals("Tipo de transação inválido para transferência.", exception.getMessage());
        verify(transacaoGateway, never()).registrarTransacao(any(Transacao.class));
        verify(contaGateway, never()).salvar(any(Conta.class));
    }

    @Test
    public void deveRealizarTransferenciaComSucesso() throws Exception {
        Long contaOrigemId = 1L;
        Long contaDestinoId = 2L;
        BigDecimal valorTransferencia = BigDecimal.valueOf(100);
        Conta contaOrigem = new Conta(contaOrigemId, "12345", TipoConta.CORRENTE, BigDecimal.valueOf(200), 1L);
        Conta contaDestino = new Conta(contaDestinoId, "67890", TipoConta.CORRENTE, BigDecimal.valueOf(100), 2L);
        Transacao transacao = new Transacao(null, TipoTransacao.TRANSFERENCIA, valorTransferencia, null, contaOrigemId, contaDestinoId);

        when(contaGateway.obterContaPorId(contaOrigemId)).thenReturn(contaOrigem);
        when(contaGateway.obterContaPorId(contaDestinoId)).thenReturn(contaDestino);

        Transacao resultado = realizarTransferencia.execute(transacao);

        assertNotNull(resultado);
        assertEquals(valorTransferencia, resultado.getValor());
        assertEquals(contaOrigemId, resultado.getContaOrigemId());
        assertEquals(contaDestinoId, resultado.getContaDestinoId());

        verify(contaGateway).salvar(contaOrigem);
        verify(contaGateway).salvar(contaDestino);
        verify(transacaoGateway).registrarTransacao(any(Transacao.class));
    }

}
