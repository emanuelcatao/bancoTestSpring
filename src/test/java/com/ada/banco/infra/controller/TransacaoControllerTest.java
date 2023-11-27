package com.ada.banco.infra.controller;

import com.ada.banco.domain.model.Transacao;
import com.ada.banco.domain.model.enums.TipoTransacao;
import com.ada.banco.domain.usecase.transacao.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(TransacaoController.class)
public class TransacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @MockBean
    private RealizarDeposito realizarDeposito;
    @MockBean
    private RealizarSaque realizarSaque;
    @MockBean
    private RealizarTransferencia realizarTransferencia;
    @MockBean
    private ListarTodasAsTransacoesPorConta listarTodasAsTransacoesPorConta;
    @MockBean
    private ObterTransacaoPorId obterTransacaoPorId;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void quandoRealizarDeposito_entaoRetornaStatusOk() throws Exception {
        Transacao transacao = new Transacao(null, TipoTransacao.DEPOSITO, new BigDecimal("100.00"), null, 1L, null);
        String jsonTransacao = objectMapper.writeValueAsString(transacao);

        given(realizarDeposito.execute(any(Transacao.class))).willReturn(transacao);

        mockMvc.perform(post("/transacao/deposito")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTransacao))
                .andExpect(status().isOk())
                .andExpect(content().string("Depósito realizado com sucesso."));

        verify(realizarDeposito, times(1)).execute(any(Transacao.class));
    }

    @Test
    public void quandoTentarRealizarDepositoInvalido_entaoRetornaBadRequest() throws Exception {
        Transacao transacao = new Transacao(null, TipoTransacao.DEPOSITO, new BigDecimal("100.00"), null, 1L, null);
        String jsonTransacao = objectMapper.writeValueAsString(transacao);

        given(realizarDeposito.execute(any(Transacao.class))).willThrow(new Exception("Conta não encontrada para depósito."));

        mockMvc.perform(post("/transacao/deposito")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTransacao))
                .andExpect(status().isBadRequest());

        verify(realizarDeposito, times(1)).execute(any(Transacao.class));
    }

    @Test
    public void quandoRealizarSaque_entaoRetornaStatusOk() throws Exception {
        Transacao transacao = new Transacao(null, TipoTransacao.SAQUE, new BigDecimal("100.00"), null, 1L, null);
        String jsonTransacao = objectMapper.writeValueAsString(transacao);

        given(realizarSaque.execute(any(Transacao.class))).willReturn(transacao);

        mockMvc.perform(post("/transacao/saque")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTransacao))
                .andExpect(status().isOk())
                .andExpect(content().string("Saque realizado com sucesso."));

        verify(realizarSaque, times(1)).execute(any(Transacao.class));
        verify(realizarDeposito, never()).execute(any(Transacao.class));
        verify(realizarTransferencia, never()).execute(any(Transacao.class));
    }

    @Test
    public void quandoTentarRealizarSaqueInvalido_entaoRetornaBadRequest() throws Exception {
        Transacao transacao = new Transacao(null, TipoTransacao.SAQUE, new BigDecimal("100.00"), null, 1L, null);
        String jsonTransacao = objectMapper.writeValueAsString(transacao);

        given(realizarSaque.execute(any(Transacao.class))).willThrow(new Exception("Saldo insuficiente para saque."));

        mockMvc.perform(post("/transacao/saque")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTransacao))
                .andExpect(status().isBadRequest());

        verify(realizarSaque, times(1)).execute(any(Transacao.class));
        verify(realizarDeposito, never()).execute(any(Transacao.class));
        verify(realizarTransferencia, times(0)).execute(any(Transacao.class));
    }
    @Test
    public void quandoRealizarTransferencia_entaoRetornaStatusOk() throws Exception {
        Transacao transacao = new Transacao(null, TipoTransacao.TRANSFERENCIA, new BigDecimal("100.00"), null, 1L, 2L);
        String jsonTransacao = objectMapper.writeValueAsString(transacao);

        given(realizarTransferencia.execute(any(Transacao.class))).willReturn(transacao);

        mockMvc.perform(post("/transacao/transferencia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTransacao))
                .andExpect(status().isOk())
                .andExpect(content().string("Transferência realizada com sucesso."));

        verify(realizarTransferencia, times(1)).execute(any(Transacao.class));
        verify(realizarDeposito, never()).execute(any(Transacao.class));
        verify(realizarSaque, times(0)).execute(any(Transacao.class));
    }

    @Test
    public void quandoTentarRealizarTransferenciaInvalida_entaoRetornaBadRequest() throws Exception {
        Transacao transacao = new Transacao(null, TipoTransacao.TRANSFERENCIA, new BigDecimal("100.00"), null, 1L, null);
        String jsonTransacao = objectMapper.writeValueAsString(transacao);

        given(realizarTransferencia.execute(any(Transacao.class))).willThrow(new Exception("Saldo insuficiente para transferência."));

        mockMvc.perform(post("/transacao/transferencia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTransacao))
                .andExpect(status().isBadRequest());

        verify(realizarTransferencia, times(1)).execute(any(Transacao.class));
        verify(realizarDeposito, never()).execute(any(Transacao.class));
        verify(realizarSaque, times(0)).execute(any(Transacao.class));
    }

    @Test
    public void quandoListarTransacoesComSucesso_entaoRetornaStatusOk() throws Exception {
        Long contaId = 1L;
        List<Transacao> transacoes = List.of(new Transacao(1L, TipoTransacao.DEPOSITO, new BigDecimal("100.00"), LocalDateTime.now(), 1L, null));
        given(listarTodasAsTransacoesPorConta.execute(contaId)).willReturn(transacoes);

        mockMvc.perform(get("/transacao/conta/{contaId}", contaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));

        verify(listarTodasAsTransacoesPorConta, times(1)).execute(contaId);
    }

    @Test
    public void quandoListarTransacoesComFalha_entaoRetornaStatusBadRequest() throws Exception {
        Long contaId = 1L;
        doThrow(new RuntimeException("Conta não encontrada.")).when(listarTodasAsTransacoesPorConta).execute(contaId);

        mockMvc.perform(get("/transacao/conta/{contaId}", contaId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Conta não encontrada."));

        verify(listarTodasAsTransacoesPorConta, times(1)).execute(contaId);
    }

    @Test
    public void quandoTentarObterTransacaoComSucesso_entaoRetornaOk() throws Exception {
        Transacao transacao = new Transacao(1L, TipoTransacao.DEPOSITO, new BigDecimal("100.00"), LocalDateTime.now(), 1L, null);

        given(obterTransacaoPorId.execute(1L)).willReturn(transacao);

        mockMvc.perform(get("/transacao/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(obterTransacaoPorId, times(1)).execute(1L);
        verify(listarTodasAsTransacoesPorConta, never()).execute(anyLong());
    }

    @Test
    public void quandoTentarObterTransacaoComSucesso_entaoRetornaBadRequest() throws Exception {
        doThrow(new RuntimeException("Transação não encontrada.")).when(obterTransacaoPorId).execute(1L);

        mockMvc.perform(get("/transacao/{id}", 1L))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Transação não encontrada."));

        verify(obterTransacaoPorId, times(1)).execute(1L);
        verify(listarTodasAsTransacoesPorConta, never()).execute(anyLong());
    }

}
