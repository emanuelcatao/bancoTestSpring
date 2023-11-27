package com.ada.banco.domain.usecase.transacao;

import com.ada.banco.domain.gateway.TransacaoGateway;
import com.ada.banco.domain.model.Transacao;
import com.ada.banco.domain.model.enums.TipoTransacao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
public class ObterTransacaoPorIdTest {

    @Mock
    private TransacaoGateway transacaoGateway;
    @InjectMocks
    private ObterTransacaoPorId obterTransacaoPorId;

    @Test
    public void deveObterTransacaoPorId() {
        Long idTransacao = 1L;
        Transacao transacaoEsperada = new Transacao(idTransacao, TipoTransacao.DEPOSITO, BigDecimal.valueOf(100), LocalDateTime.now(), 1L, null);

        when(transacaoGateway.obterTransacaoPorId(idTransacao)).thenReturn(transacaoEsperada);

        Transacao transacaoObtida = obterTransacaoPorId.execute(idTransacao);

        assertNotNull(transacaoObtida);
        assertEquals(transacaoEsperada, transacaoObtida);
        verify(transacaoGateway).obterTransacaoPorId(idTransacao);
    }
}
