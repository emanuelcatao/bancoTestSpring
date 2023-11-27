package com.ada.banco.domain.usecase.conta;

import com.ada.banco.domain.gateway.ClienteGateway;
import com.ada.banco.domain.gateway.ContaGateway;
import com.ada.banco.domain.model.Cliente;
import com.ada.banco.domain.model.Conta;
import com.ada.banco.domain.model.enums.TipoConta;
import com.ada.banco.domain.usecase.utils.GerarNumeroDeContaUnico;
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
    @Mock
    private GerarNumeroDeContaUnico gerarNumeroDeContaUnico;
    @InjectMocks
    private CriarNovaConta criarNovaConta;

    @Test
    public void deveLancarExceptionQuandoClienteNaoExistir() {
        Mockito.when(clienteGateway.buscarPorId(1L)).thenReturn(null);

        Exception exception = assertThrows(
                Exception.class,
                () -> criarNovaConta.execute(new Conta(null, null, TipoConta.CORRENTE, BigDecimal.ZERO, 1L))
        );

        assertEquals("Cliente não encontrado para o Id informado.", exception.getMessage());

        verify(clienteGateway, times(1)).buscarPorId(1L);
        verify(contaGateway, never()).obterContaPorIdCliente(1L);
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

        verify(clienteGateway, times(1)).buscarPorId(idCliente);
        verify(contaGateway, times(1)).obterContaPorIdCliente(idCliente);
        verify(contaGateway, never()).salvar(any(Conta.class));
    }

    @Test
    public void deveCriarUmaNovaContaComSucessoQuandoClienteExistirEContaInexistente() throws Exception {
        Long idCliente = 1L;
        Cliente clienteExistente = new Cliente(idCliente, "Cliente Teste", "98765432100");
        String numeroContaUnico = "67890";

        Mockito.when(clienteGateway.buscarPorId(idCliente)).thenReturn(clienteExistente);
        Mockito.when(contaGateway.obterContaPorIdCliente(idCliente)).thenReturn(null);
        Mockito.when(gerarNumeroDeContaUnico.execute()).thenReturn(numeroContaUnico);
        Mockito.when(contaGateway.salvar(any(Conta.class))).thenAnswer(i -> i.getArguments()[0]);

        Conta contaCriada = criarNovaConta.execute(new Conta(null, null, TipoConta.CORRENTE, BigDecimal.ZERO, idCliente));

        assertNotNull(contaCriada);
        assertEquals(numeroContaUnico, contaCriada.getNumeroConta());
        assertEquals(idCliente, contaCriada.getIdCliente());

        verify(clienteGateway).buscarPorId(idCliente);
        verify(contaGateway).obterContaPorIdCliente(idCliente);
        verify(gerarNumeroDeContaUnico).execute();
        verify(contaGateway).salvar(any(Conta.class));
    }

}