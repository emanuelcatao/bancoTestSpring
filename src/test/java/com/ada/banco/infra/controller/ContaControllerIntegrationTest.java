package com.ada.banco.infra.controller;

import com.ada.banco.domain.model.Conta;
import com.ada.banco.domain.model.TipoConta;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations="classpath:application-test.properties")
public class ContaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Sql("/sql/insert_cliente.sql")
    public void testAbrirConta() throws Exception {
        Conta novaConta = new Conta(null, "12345", TipoConta.CORRENTE, new BigDecimal("1000"), 1L);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonConta = objectMapper.writeValueAsString(novaConta);

        mockMvc.perform(post("/contas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonConta))
                .andExpect(status().isCreated());
    }

    @Test
    @Sql("/sql/insert_conta.sql")
    public void testObterConta() throws Exception {
        mockMvc.perform(get("/contas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testListarContas() throws Exception {
        mockMvc.perform(get("/contas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
