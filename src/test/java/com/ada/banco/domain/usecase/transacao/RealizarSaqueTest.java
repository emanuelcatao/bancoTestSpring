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
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class RealizarSaqueTest {

    @Mock
    private ContaGateway contaGateway;
    @Mock
    private TransacaoGateway transacaoGateway;
    @InjectMocks
    private RealizarSaque realizarSaque;

    @Test
    public void deveLancarExceptionParaValorInvalido() {
        Transacao transacaoInvalida = new Transacao(1L, TipoTransacao.SAQUE, BigDecimal.valueOf(-100), null, 1L, null);

        Exception exception = assertThrows(
                Exception.class,
                () -> realizarSaque.execute(transacaoInvalida)
        );

        assertEquals("Valor inválido para saque.", exception.getMessage());
    }

    @Test
    public void deveLancarExceptionParaContaInexistente() {
        Transacao transacao = new Transacao(1L, TipoTransacao.SAQUE, BigDecimal.valueOf(100), null, 1L, null);
        when(contaGateway.obterContaPorId(1L)).thenReturn(null);

        Exception exception = assertThrows(
                Exception.class,
                () -> realizarSaque.execute(transacao)
        );

        assertEquals("Conta não encontrada para saque.", exception.getMessage());
    }

    @Test
    public void deveLancarExceptionParaSaldoInsuficiente() {
        Transacao transacao = new Transacao(1L, TipoTransacao.SAQUE, BigDecimal.valueOf(500), null, 1L, null);
        Conta conta = new Conta(1L, "12345", TipoConta.POUPANCA, BigDecimal.valueOf(300), 1L);
        when(contaGateway.obterContaPorId(1L)).thenReturn(conta);

        Exception exception = assertThrows(
                Exception.class,
                () -> realizarSaque.execute(transacao)
        );

        assertEquals("Saldo insuficiente para saque.", exception.getMessage());
    }

    @Test
    public void deveRealizarSaqueComSucesso() throws Exception {
        Transacao transacao = new Transacao(1L, TipoTransacao.SAQUE, BigDecimal.valueOf(500), null, 1L, null);
        Conta conta = new Conta(1L, "12345", TipoConta.CORRENTE, BigDecimal.valueOf(500), 1L);

        when(contaGateway.obterContaPorId(1L)).thenReturn(conta);
        when(transacaoGateway.obterTransacaoPorId(1L)).thenReturn(transacao);

        Transacao resultado = realizarSaque.execute(transacao);

        assertEquals(BigDecimal.valueOf(0), conta.getSaldo());
        assertEquals(LocalDateTime.now().getDayOfYear(), resultado.getData().getDayOfYear());
        assertEquals(BigDecimal.valueOf(500), resultado.getValor());
        assertEquals(TipoTransacao.SAQUE, resultado.getTipo());

        verify(contaGateway, times(1)).obterContaPorId(1L);
        verify(contaGateway, times(1)).salvar(any(Conta.class));
    }

    @Test
    public void deveLancarExceptionSeTipoTransacaoDiferenteDeSaque() {
        Transacao transacao = new Transacao(1L, TipoTransacao.TRANSFERENCIA, BigDecimal.valueOf(100), null, 1L, 2L);
        when(contaGateway.obterContaPorId(1L)).thenReturn(new Conta(1L, "12345", TipoConta.CORRENTE, BigDecimal.valueOf(200), 1L));

        Exception exception = assertThrows(
                Exception.class,
                () -> realizarSaque.execute(transacao)
        );

        assertEquals("Tipo de transação inválido para saque.", exception.getMessage());
        verify(transacaoGateway, never()).registrarTransacao(any(Transacao.class));
        verify(contaGateway, never()).salvar(any(Conta.class));
    }
}
