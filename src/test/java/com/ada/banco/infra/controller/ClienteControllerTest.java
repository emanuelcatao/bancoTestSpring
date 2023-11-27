package com.ada.banco.infra.controller;

import com.ada.banco.domain.model.Cliente;
import com.ada.banco.domain.usecase.cliente.BuscarClientePorCpf;
import com.ada.banco.domain.usecase.cliente.CadastrarNovoCliente;
import com.ada.banco.domain.usecase.cliente.ListarTodosOsClientes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ClienteController.class)
public class ClienteControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @MockBean
    private BuscarClientePorCpf buscarClientePorCpf;
    @MockBean
    private CadastrarNovoCliente cadastrarNovoCliente;
    @MockBean
    private ListarTodosOsClientes listarTodosOsClientes;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void quandoBuscarCliente_entaoRetornaStatusOk() throws Exception {
        String cpf = "12345678900";
        Cliente clienteEsperado = new Cliente(1L, "Cliente Teste", cpf);
        given(buscarClientePorCpf.execute(cpf)).willReturn(clienteEsperado);

        mockMvc.perform(get("/clientes/{cpf}", cpf))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Cliente Teste"))
                .andExpect(jsonPath("$.cpf").value(cpf));

        verify(buscarClientePorCpf, times(1)).execute(cpf);
    }

    @Test
    public void quandoCriarCliente_entaoRetornaStatusCreated() throws Exception {
        Cliente novoCliente = new Cliente(null, "Novo Cliente", "448.046.050-02");
        String jsonCliente = objectMapper.writeValueAsString(novoCliente);

        given(cadastrarNovoCliente.execute(any(Cliente.class))).willReturn(novoCliente);

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCliente))
                .andExpect(status().isCreated())
                .andExpect(content().string("Cliente criado com sucesso!"));

        verify(cadastrarNovoCliente, times(1)).execute(any(Cliente.class));
    }

    @Test
    public void quandoListarClientes_entaoRetornaStatusOk() throws Exception {
        List<Cliente> clientesEsperados = List.of(new Cliente(1L, "Cliente Teste", "12345678900"));
        given(listarTodosOsClientes.execute()).willReturn(clientesEsperados);

        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Cliente Teste"))
                .andExpect(jsonPath("$[0].cpf").value("12345678900"));

        verify(listarTodosOsClientes, times(1)).execute();
        verify(buscarClientePorCpf, never()).execute(anyString());
    }

    @Test
    public void quandoBuscarClienteInexistente_entaoRetornaNotFound() throws Exception {
        String cpfInexistente = "00000000000";
        given(buscarClientePorCpf.execute(cpfInexistente)).willReturn(null);

        mockMvc.perform(get("/clientes/{cpf}", cpfInexistente))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
        verify(buscarClientePorCpf, times(1)).execute(cpfInexistente);
    }

    @Test
    public void quandoCriarClienteInvalido_entaoRetornaBadRequest() throws Exception {
        Cliente clienteInvalido = new Cliente(null, "nome", "123");
        String jsonClienteInvalido = objectMapper.writeValueAsString(clienteInvalido);

        given(cadastrarNovoCliente.execute(any(Cliente.class))).willThrow(new IllegalArgumentException());

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonClienteInvalido))
                .andExpect(status().isBadRequest());

        verify(cadastrarNovoCliente, times(1)).execute(any(Cliente.class));
    }

    @Test
    public void quandoListarClientesVazia_entaoRetornaOkComListaVazia() throws Exception {
        given(listarTodosOsClientes.execute()).willReturn(Collections.emptyList());

        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(listarTodosOsClientes, times(1)).execute();
    }
}
