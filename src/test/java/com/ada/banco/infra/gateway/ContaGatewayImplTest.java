package com.ada.banco.infra.gateway;

import com.ada.banco.domain.model.Conta;
import com.ada.banco.domain.model.enums.TipoConta;
import com.ada.banco.infra.gateway.bd.ContaGatewayImpl;
import com.ada.banco.infra.gateway.jpa.ContaEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ContaGatewayImplTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private ContaGatewayImpl contaGateway;

    // auxiliar
    private Conta toDomain(ContaEntity contaEntity) {
        return new Conta(
                contaEntity.getId(),
                contaEntity.getNumeroConta(),
                contaEntity.getTipoConta(),
                contaEntity.getSaldo(),
                contaEntity.getIdCliente()
        );
    }

    private ContaEntity toEntity(Conta conta) {
        return new ContaEntity(
                conta.getId(),
                conta.getNumeroConta(),
                conta.getTipoConta(),
                conta.getSaldo(),
                conta.getIdCliente()
        );
    }

    @Test
    public void quandoBuscarPorCpf_entaoRetornaConta() {
        String cpf = "12345678900";
        Long idCliente = 1L;
        ContaEntity contaEntity = new ContaEntity(1L, "12345", TipoConta.CORRENTE, new BigDecimal("1000.00"), idCliente);

        TypedQuery<Long> idClienteQuery = mock(TypedQuery.class);
        TypedQuery<ContaEntity> contaQuery = mock(TypedQuery.class);

        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(idClienteQuery);
        when(idClienteQuery.setParameter("cpf", cpf)).thenReturn(idClienteQuery);
        when(idClienteQuery.getSingleResult()).thenReturn(idCliente);

        when(entityManager.createQuery(anyString(), eq(ContaEntity.class))).thenReturn(contaQuery);
        when(contaQuery.setParameter("idCliente", idCliente)).thenReturn(contaQuery);
        when(contaQuery.getSingleResult()).thenReturn(contaEntity);

        Conta result = contaGateway.buscarPorCpf(cpf);

        assertNotNull(result);
        assertEquals(contaEntity.getNumeroConta(), result.getNumeroConta());

        verify(entityManager).createQuery(anyString(), eq(Long.class));
        verify(idClienteQuery).setParameter("cpf", cpf);
        verify(idClienteQuery).getSingleResult();
    }

    @Test
    public void quandoBuscarPorCpfENaoEncontrar_entaoRetornaNull() {
        String cpf = "12345678900";

        TypedQuery<Long> idClienteQuery = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(idClienteQuery);
        when(idClienteQuery.setParameter("cpf", cpf)).thenReturn(idClienteQuery);
        when(idClienteQuery.getSingleResult()).thenThrow(NoResultException.class);

        Conta result = contaGateway.buscarPorCpf(cpf);

        assertNull(result);

        verify(entityManager).createQuery(anyString(), eq(Long.class));
        verify(idClienteQuery).setParameter("cpf", cpf);
        verify(idClienteQuery).getSingleResult();
    }
}
