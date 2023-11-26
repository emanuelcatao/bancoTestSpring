package com.ada.banco.domain.usecase.cliente;

import com.ada.banco.domain.gateway.ClienteGateway;
import com.ada.banco.domain.model.Cliente;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BuscarClientePorCpfTest {

    @Mock
    private ClienteGateway clienteGateway;

    @InjectMocks
    private BuscarClientePorCpf buscarClientePorCpf;

    @Test
    public void deveRetornarClienteQuandoCpfValido() {
        String cpf = "12345678900";
        Cliente clienteMock = new Cliente(1L, "Cliente Teste", cpf);
        Mockito.when(clienteGateway.buscarPorCpf(cpf)).thenReturn(clienteMock);

        Cliente cliente = buscarClientePorCpf.execute(cpf);

        assertNotNull(cliente);
        assertEquals(cpf, cliente.getCpf());
        verify(clienteGateway, times(1)).buscarPorCpf(cpf);
    }

    @Test
    public void deveRetornarNullQuandoCpfInvalido() {
        String cpf = "00000000000";
        Mockito.when(clienteGateway.buscarPorCpf(cpf)).thenReturn(null);

        Cliente cliente = buscarClientePorCpf.execute(cpf);

        assertNull(cliente);
        verify(clienteGateway, times(1)).buscarPorCpf(cpf);
    }
}
