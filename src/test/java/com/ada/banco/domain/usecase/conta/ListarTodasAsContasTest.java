package com.ada.banco.domain.usecase.conta;

import com.ada.banco.domain.gateway.ContaGateway;
import com.ada.banco.domain.model.Conta;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.ada.banco.domain.model.TipoConta;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ListarTodasAsContasTest {

    @Mock
    private ContaGateway contaGateway;

    @InjectMocks
    private ListarTodasAsContas listarTodasAsContas;

    @Test
    public void deveRetornarListaVaziaQuandoNenhumaContaExistir() {
        when(contaGateway.listarTodas()).thenReturn(Collections.emptyList());

        List<Conta> contas = listarTodasAsContas.execute();

        assertTrue(contas.isEmpty());
        verify(contaGateway, times(1)).listarTodas();
    }

    @Test
    public void deveRetornarListaDeContasQuandoContasExistirem() {
        List<Conta> contasMock = Arrays.asList(
                new Conta(1L, "12345", TipoConta.CORRENTE, BigDecimal.ZERO, 1L),
                new Conta(2L, "67890", TipoConta.POUPANCA, BigDecimal.valueOf(1000), 2L)
        );
        when(contaGateway.listarTodas()).thenReturn(contasMock);

        List<Conta> contas = listarTodasAsContas.execute();

        assertFalse(contas.isEmpty());
        assertEquals(2, contas.size());
        verify(contaGateway, times(1)).listarTodas();
    }
}
