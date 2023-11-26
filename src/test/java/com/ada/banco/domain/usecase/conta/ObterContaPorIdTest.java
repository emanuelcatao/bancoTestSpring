package com.ada.banco.domain.usecase.conta;

import com.ada.banco.domain.gateway.ContaGateway;
import com.ada.banco.domain.model.Conta;
import com.ada.banco.domain.model.TipoConta;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ObterContaPorIdTest {

    @Mock
    private ContaGateway contaGateway;

    @InjectMocks
    private ObterContaPorId obterContaPorId;

    @Test
    public void deveRetornarContaQuandoEncontrada() {
        Long idConta = 1L;
        Conta contaMock = new Conta(idConta, "12345", TipoConta.CORRENTE, BigDecimal.ZERO, 1L);
        Mockito.when(contaGateway.obterContaPorId(idConta)).thenReturn(contaMock);

        Conta conta = obterContaPorId.execute(idConta);

        assertNotNull(conta);
        assertEquals(idConta, conta.getId());
        verify(contaGateway, times(1)).obterContaPorId(idConta);
    }

    @Test
    public void deveLancarRuntimeExceptionQuandoContaNaoEncontrada() {
        Long idConta = 1L;
        Mockito.when(contaGateway.obterContaPorId(idConta)).thenReturn(null);

        Exception exception = assertThrows(
                RuntimeException.class,
                () -> obterContaPorId.execute(idConta)
        );

        assertEquals("Conta não encontrada", exception.getMessage());
        verify(contaGateway, times(1)).obterContaPorId(idConta);
    }
}
