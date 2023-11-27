package com.ada.banco.infra.gateway;

import com.ada.banco.domain.model.Conta;
import com.ada.banco.domain.model.Transacao;
import com.ada.banco.domain.model.enums.TipoConta;
import com.ada.banco.domain.model.enums.TipoTransacao;
import com.ada.banco.infra.gateway.bd.TransacaoGatewayImpl;
import com.ada.banco.infra.gateway.jpa.TransacaoEntity;
import jakarta.persistence.EntityManager;
import org.hibernate.query.Query;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TransacaoGatewayImplTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private TransacaoGatewayImpl gateway;


    @Test
    public void quandoRegistrarTransacao_entaoRetornaTrue() {
        Transacao transacao = new Transacao(null, TipoTransacao.DEPOSITO, new BigDecimal("100.00"), LocalDateTime.now(), 1L, null);

        doAnswer(invocation -> {
            TransacaoEntity entity = invocation.getArgument(0);
            entity.setId(1L); // Simular a geração do ID
            return null; // Retornar void (null neste caso)
        }).when(entityManager).persist(any(TransacaoEntity.class));

        Transacao result = gateway.registrarTransacao(transacao);

        assertNotNull(result.getId());
        assertEquals(transacao.getTipo(), result.getTipo());
        assertEquals(transacao.getValor(), result.getValor());
        assertEquals(transacao.getData(), result.getData());
        assertEquals(transacao.getContaOrigemId(), result.getContaOrigemId());
        assertEquals(transacao.getContaDestinoId(), result.getContaDestinoId());
        verify(entityManager).persist(any(TransacaoEntity.class));
    }

    @Test
    public void quandoObterTransacaoPorId_entaoRetornaTransacao() {
        Long id = 1L;
        TransacaoEntity transacaoEntity = new TransacaoEntity(id, TipoTransacao.DEPOSITO, new BigDecimal("100.00"), LocalDateTime.now(), 1L, null);

        when(entityManager.find(TransacaoEntity.class, id)).thenReturn(transacaoEntity);

        Transacao result = gateway.obterTransacaoPorId(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(entityManager).find(TransacaoEntity.class, id);
    }

    @Test
    public void quandoListarTodasAsTransacoesDaConta_entaoRetornaLista() {
        Conta conta = new Conta(1L, "12345", TipoConta.CORRENTE, new BigDecimal("1000.00"), 1L);
        List<TransacaoEntity> transacoes = List.of(
                new TransacaoEntity(1L, TipoTransacao.DEPOSITO, new BigDecimal("100.00"), LocalDateTime.now(), conta.getId(), null)
        );

        when(entityManager.createQuery(anyString(), eq(TransacaoEntity.class))).then(invocation -> {
            Query mockQuery = mock(Query.class);
            when(mockQuery.setParameter(anyString(), any())).thenReturn(mockQuery);
            when(mockQuery.getResultList()).thenReturn(transacoes);
            return mockQuery;
        });

        List<Transacao> result = gateway.listarTodasAsTransacoesDaConta(conta);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(entityManager).createQuery(anyString(), eq(TransacaoEntity.class));
    }

}
