package com.ada.banco.domain.usecase.cliente;

import com.ada.banco.domain.gateway.ClienteGateway;
import com.ada.banco.domain.model.Cliente;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CadastrarNovoClienteTest {

    @Mock
    private ClienteGateway clienteGateway;

    @InjectMocks
    private CadastrarNovoCliente cadastrarNovoCliente;

    @Test
    public void deveCadastrarClienteQuandoNaoExistir() {
        Cliente novoCliente = new Cliente(null, "Cliente Novo", "12345678900");
        Mockito.when(clienteGateway.buscarPorCpf("12345678900")).thenReturn(null);

        cadastrarNovoCliente.execute(novoCliente);

        verify(clienteGateway, times(1)).buscarPorCpf("12345678900");
        verify(clienteGateway, times(1)).salvar(novoCliente);
    }

    @Test
    public void deveLancarRuntimeExceptionQuandoClienteJaCadastrado() {
        Cliente clienteExistente = new Cliente(1L, "Cliente Existente", "12345678900");
        Mockito.when(clienteGateway.buscarPorCpf("12345678900")).thenReturn(clienteExistente);

        Exception exception = assertThrows(
                RuntimeException.class,
                () -> cadastrarNovoCliente.execute(new Cliente(null, "Outro Cliente", "12345678900"))
        );

        assertEquals("Cliente já cadastrado", exception.getMessage());
        verify(clienteGateway, times(1)).buscarPorCpf("12345678900");
        verify(clienteGateway, never()).salvar(any(Cliente.class));
    }
}
