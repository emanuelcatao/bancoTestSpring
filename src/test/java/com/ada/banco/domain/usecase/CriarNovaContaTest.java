package com.ada.banco.domain.usecase;

import com.ada.banco.domain.gateway.ClienteGateway;
import com.ada.banco.domain.gateway.ContaGateway;
import com.ada.banco.domain.model.Cliente;
import com.ada.banco.domain.model.Conta;
import com.ada.banco.domain.model.TipoConta;
import com.ada.banco.domain.usecase.conta.CriarNovaConta;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class CriarNovaContaTest {
    @Mock
    private ContaGateway contaGateway;
    @Mock
    private ClienteGateway clienteGateway;
    @InjectMocks
    private CriarNovaConta criarNovaConta;

    @Test
    public void deveLancarExceptionQuandoClienteNaoExistir() {
        Mockito.when(clienteGateway.buscarPorId(1L)).thenReturn(null);

        Exception exception = assertThrows(
                Exception.class,
                () -> criarNovaConta.execute(new Conta(null, "12345", TipoConta.CORRENTE, BigDecimal.ZERO, 1L))
        );

        assertEquals("Cliente não encontrado para o Id informado.", exception.getMessage());
        verify(contaGateway, never()).salvar(any(Conta.class));
    }

    @Test
    public void deveLancarExceptionQuandoClienteJaPossuirConta() {
        Long idCliente = 1L;
        Cliente clienteExistente = new Cliente(idCliente, "Cliente Teste", "98765432100");
        Conta novaConta = new Conta(1L, "12345", TipoConta.CORRENTE, BigDecimal.ZERO, idCliente);

        Mockito.when(clienteGateway.buscarPorId(1L)).thenReturn(clienteExistente);
        Mockito.when(contaGateway.obterContaPorIdCliente(idCliente)).thenReturn(novaConta);

        Exception exception = assertThrows(
                Exception.class,
                () -> criarNovaConta.execute(novaConta)
        );

        assertEquals("Usuário já possui uma conta", exception.getMessage());
        verify(contaGateway, never()).salvar(any(Conta.class));
    }

    @Test
    public void deveCriarUmaNovaContaComSucessoQuandoClienteExistirEContaInexistente() throws Exception {
        Long idCliente = 1L;
        Cliente clienteExistente = new Cliente(idCliente, "Cliente Teste", "98765432100");
        Conta novaConta = new Conta(null, "12345", TipoConta.CORRENTE, BigDecimal.ZERO, idCliente);

        Mockito.when(clienteGateway.buscarPorId(idCliente)).thenReturn(clienteExistente);
        Mockito.when(contaGateway.obterContaPorIdCliente(idCliente)).thenReturn(null);
        Mockito.when(contaGateway.salvar(any(Conta.class))).thenReturn(novaConta);

        Conta contaCriada = criarNovaConta.execute(novaConta);

        assertNotNull(contaCriada);
        assertEquals(idCliente, contaCriada.getIdCliente());
        assertEquals("12345", contaCriada.getNumeroConta());
        verify(contaGateway).salvar(novaConta);
    }

}