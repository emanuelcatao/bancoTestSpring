package com.ada.banco.infra.controller;

import com.ada.banco.domain.model.Conta;
import com.ada.banco.domain.model.enums.TipoConta;
import com.ada.banco.domain.usecase.conta.CriarNovaConta;
import com.ada.banco.domain.usecase.conta.ListarTodasAsContas;
import com.ada.banco.domain.usecase.conta.ObterContaPorId;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.BDDMockito.*;

/**
 * Classe de testes de unidade do controller de contas.
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(ContaController.class)
public class ContaControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @MockBean
    private CriarNovaConta criarNovaContaUseCase;
    @MockBean
    private ListarTodasAsContas listarTodasAsContasUseCase;
    @MockBean
    private ObterContaPorId obterContaPorIdUseCase;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    /**
     * Teste unitário que valida o comportamento do controller ao criar uma nova conta.
     * @throws Exception Exceção lançada caso ocorra algum erro ao criar a conta.
     */
    @Test
    public void quandoCriarConta_entaoRetornaStatusCreated() throws Exception {
        Conta novaConta = new Conta(null, "12345", TipoConta.CORRENTE, new BigDecimal("1000.00"), 1L);
        String novaContaJson = objectMapper.writeValueAsString(novaConta);

        // Mock do comportamento do use case
        given(criarNovaContaUseCase.execute(any(Conta.class))).willReturn(novaConta);

        mockMvc.perform(post("/contas")
                        .contentType("application/json")
                        .content(novaContaJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numeroConta").value("12345"));

        verify(criarNovaContaUseCase, times(1)).execute(any(Conta.class));
        verify(obterContaPorIdUseCase, never()).execute(anyLong());
        verify(listarTodasAsContasUseCase, never()).execute();
    }

    /**
     * Teste unitário que valida o comportamento do controller ao obter uma conta.
     * @throws Exception Exceção lançada caso ocorra algum erro ao obter a conta.
     */
    @Test
    public void quandoObterConta_entaoRetornaStatusOk() throws Exception {
        Conta conta = new Conta(1L, "12345", TipoConta.CORRENTE, new BigDecimal("1000.00"), 1L);

        // Mock do comportamento do use case
        given(obterContaPorIdUseCase.execute(1L)).willReturn(conta);

        mockMvc.perform(get("/contas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroConta").value("12345"));

        verify(obterContaPorIdUseCase, times(1)).execute(anyLong());
        verify(criarNovaContaUseCase, never()).execute(any(Conta.class));
        verify(listarTodasAsContasUseCase, never()).execute();
    }

    /**
     * Teste unitário que valida o comportamento do controller ao listar todas as contas.
     * @throws Exception Exceção lançada caso ocorra algum erro ao listar as contas.
     */
    @Test
    public void quandoListarContas_entaoRetornaStatusOk() throws Exception {
        Conta conta1 = new Conta(1L, "12345", TipoConta.CORRENTE, new BigDecimal("1000.00"), 1L);
        Conta conta2 = new Conta(2L, "54321", TipoConta.POUPANCA, new BigDecimal("2000.00"), 2L);

        // Mock do comportamento do use case
        given(listarTodasAsContasUseCase.execute()).willReturn(List.of(conta1, conta2));

        mockMvc.perform(get("/contas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].numeroConta").value("12345"))
                .andExpect(jsonPath("$[1].numeroConta").value("54321"));

        verify(listarTodasAsContasUseCase, times(1)).execute();
        verify(obterContaPorIdUseCase, never()).execute(anyLong());
        verify(criarNovaContaUseCase, never()).execute(any(Conta.class));
    }

    /**
     * Teste unitário que valida o comportamento do controller ao criar uma nova conta com dados inválidos.
     * @throws Exception Exceção lançada caso ocorra algum erro ao criar a conta.
     */
    @Test
    public void quandoCriarContaComDadosInvalidos_entaoRetornaStatusBadRequest() throws Exception {
        Conta novaConta = new Conta(null, "12345", TipoConta.CORRENTE, new BigDecimal("1000.00"), 1L);
        String novaContaJson = objectMapper.writeValueAsString(novaConta);

        // Mock do comportamento do use case
        given(criarNovaContaUseCase.execute(any(Conta.class))).willThrow(new Exception("Erro ao criar conta"));

        mockMvc.perform(post("/contas")
                        .contentType("application/json")
                        .content(novaContaJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Erro ao abrir conta: Erro ao criar conta"));

        verify(criarNovaContaUseCase, times(1)).execute(any(Conta.class));
        verify(obterContaPorIdUseCase, never()).execute(anyLong());
        verify(listarTodasAsContasUseCase, never()).execute();
    }

    /**
     * Teste unitário que valida o comportamento do controller ao obter uma conta inexistente.
     * @throws Exception Exceção lançada caso ocorra algum erro ao obter a conta.
     */
    @Test
    public void quandoObterContaInexistente_entaoRetornaStatusNotFound() throws Exception {
        given(obterContaPorIdUseCase.execute(1L)).willThrow(new RuntimeException("Conta não encontrada"));

        mockMvc.perform(get("/contas/1"))
                .andExpect(status().isNotFound());

        verify(obterContaPorIdUseCase, times(1)).execute(anyLong());
        verify(criarNovaContaUseCase, never()).execute(any(Conta.class));
        verify(listarTodasAsContasUseCase, never()).execute();
    }

    /**
     * Teste unitário que valida o comportamento do controller ao listar todas as contas vazias.
     * @throws Exception Exceção lançada caso ocorra algum erro ao listar as contas.
     */
    @Test
    public void quandoListarContasVazias_entaoRetornaStatusOk() throws Exception {
        // Mock do comportamento do use case
        given(listarTodasAsContasUseCase.execute()).willReturn(List.of());

        mockMvc.perform(get("/contas"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(listarTodasAsContasUseCase, times(1)).execute();
        verify(obterContaPorIdUseCase, never()).execute(anyLong());
        verify(criarNovaContaUseCase, never()).execute(any(Conta.class));
    }
}

