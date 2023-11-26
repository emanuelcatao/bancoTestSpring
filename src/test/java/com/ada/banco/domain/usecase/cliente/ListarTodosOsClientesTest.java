package com.ada.banco.domain.usecase.cliente;

import com.ada.banco.domain.gateway.ClienteGateway;
import com.ada.banco.domain.model.Cliente;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ListarTodosOsClientesTest {

    @Mock
    private ClienteGateway clienteGateway;

    @InjectMocks
    private ListarTodosOsClientes listarTodosOsClientes;

    @Test
    public void deveRetornarListaVaziaQuandoNenhumClienteCadastrado() {
        Mockito.when(clienteGateway.listarTodosOsClientes()).thenReturn(Collections.emptyList());

        List<Cliente> clientes = listarTodosOsClientes.execute();

        assertTrue(clientes.isEmpty());
        verify(clienteGateway, times(1)).listarTodosOsClientes();
    }

    @Test
    public void deveRetornarListaDeClientesQuandoExistiremClientes() {
        List<Cliente> clientesMock = Arrays.asList(
                new Cliente(1L, "Cliente 1", "12345678900"),
                new Cliente(2L, "Cliente 2", "98765432100")
        );
        Mockito.when(clienteGateway.listarTodosOsClientes()).thenReturn(clientesMock);

        List<Cliente> clientes = listarTodosOsClientes.execute();

        assertFalse(clientes.isEmpty());
        assertEquals(2, clientes.size());
        verify(clienteGateway, times(1)).listarTodosOsClientes();
    }
}

