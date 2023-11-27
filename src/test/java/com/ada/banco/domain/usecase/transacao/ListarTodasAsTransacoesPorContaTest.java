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
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ListarTodasAsTransacoesPorContaTest {

    @Mock
    private ContaGateway contaGateway;
    @Mock
    private TransacaoGateway transacaoGateway;
    @InjectMocks
    private ListarTodasAsTransacoesPorConta listarTodasAsTransacoesPorConta;

    @Test
    public void deveLancarExceptionParaContaInexistente() {
        Long idConta = 1L;
        when(contaGateway.obterContaPorId(idConta)).thenReturn(null);

        Exception exception = assertThrows(
                Exception.class,
                () -> listarTodasAsTransacoesPorConta.execute(idConta)
        );

        assertEquals("Conta não encontrada.", exception.getMessage());
    }

    @Test
    public void deveListarTransacoesParaContaExistente() throws Exception {
        Long idConta = 1L;
        Conta conta = new Conta(idConta, "12345", TipoConta.CORRENTE, BigDecimal.valueOf(200), 2L);
        List<Transacao> transacoesMock = Arrays.asList(
                new Transacao(1L, TipoTransacao.DEPOSITO, BigDecimal.valueOf(100), LocalDateTime.now(), idConta, null),
                new Transacao(2L, TipoTransacao.SAQUE, BigDecimal.valueOf(50), LocalDateTime.now(), idConta, null)
        );

        when(contaGateway.obterContaPorId(idConta)).thenReturn(conta);
        when(transacaoGateway.listarTodasAsTransacoesDaConta(conta)).thenReturn(transacoesMock);

        List<Transacao> transacoes = listarTodasAsTransacoesPorConta.execute(idConta);

        assertNotNull(transacoes);
        assertEquals(2, transacoes.size());
        verify(transacaoGateway).listarTodasAsTransacoesDaConta(conta);
    }
}
