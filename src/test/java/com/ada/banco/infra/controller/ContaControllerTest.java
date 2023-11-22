package com.ada.banco.infra.controller;

import com.ada.banco.domain.model.Conta;
import com.ada.banco.domain.model.TipoConta;
import com.ada.banco.domain.usecase.conta.CriarNovaConta;
import com.ada.banco.domain.usecase.conta.ListarTodasAsContas;
import com.ada.banco.domain.usecase.conta.ObterContaPorId;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ContaController.class)
public class ContaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CriarNovaConta criarNovaContaUseCase;
    @MockBean
    private ListarTodasAsContas listarTodasAsContasUseCase;
    @MockBean
    private ObterContaPorId obterContaPorIdUseCase;

    @Test
    public void quandoCriarConta_entaoRetornaStatusCreated() throws Exception {
        Conta novaConta = new Conta(null, "12345", TipoConta.CORRENTE, new BigDecimal("1000.00"), 1L);
        ObjectMapper objectMapper = new ObjectMapper();
        String novaContaJson = objectMapper.writeValueAsString(novaConta);

        // Mock do comportamento do use case
        given(criarNovaContaUseCase.execute(any(Conta.class))).willReturn(novaConta);

        mockMvc.perform(post("/contas")
                        .contentType("application/json")
                        .content(novaContaJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numeroConta").value("12345"));
    }
}

