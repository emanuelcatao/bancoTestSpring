package com.ada.banco.infra.controller;

import com.ada.banco.domain.model.Conta;
import com.ada.banco.domain.model.TipoConta;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Classe de testes de integração do controller de contas.
 *
 * Nota: para que os testes de integração funcionem corretamente, é necessário resetar as
 * operações no banco de dados.
 *
 * Para isso, é necessário adicionar a anotação @DirtiesContext na classe de testes e
 * definir a propriedade classMode como DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD, além
 * de spring.jpa.hibernate.ddl-auto=create-drop no arquivo application-test.properties.
 * */
@SpringBootTest // contexto da aplicação
@AutoConfigureMockMvc // configura o MockMvc
@TestPropertySource(locations="classpath:application-test.properties") // seta o arquivo ed propriedades específico para testes
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // reseta o contexto antes de cada teste
public class ContaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc; // usado para realizar as requisições
    private ObjectMapper objectMapper; // usado para serializar/deserializar objetos JSON

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    /**
     * Teste de integração que valida o comportamento do controller ao criar uma nova conta.
     * @throws Exception Exceção lançada caso ocorra algum erro ao criar a conta.
     */
    @Test
    @Sql("/sql/insert_cliente.sql") // Executa o script SQL para inserir um cliente antes de executar o teste
    public void testAbrirConta() throws Exception {
        Conta novaConta = new Conta(null, "00000", TipoConta.SALARIO, new BigDecimal("750"), 1L);

        String jsonConta = objectMapper.writeValueAsString(novaConta);

        mockMvc.perform(post("/contas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonConta))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numeroConta").value("00000"))
                .andExpect(jsonPath("$.tipoConta").value("SALARIO"))
                .andExpect(jsonPath("$.saldo").value(750));

        // Verifica se a conta foi realmente salva (nao verifica o id por conta da inconsistencia esperada, já elencada na documentacao da classe)
        mockMvc.perform(get("/contas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroConta").value("00000"))
                .andExpect(jsonPath("$.tipoConta").value("SALARIO"))
                .andExpect(jsonPath("$.saldo").value(750))
                .andExpect(jsonPath("$.idCliente").value(1));
    }

    /**
     * Teste de integração que valida o comportamento do controller ao obter uma conta.
     * @throws Exception
     */
    @Test
    @Sql("/sql/insert_conta.sql") // Executa o script SQL para inserir uma conta antes de executar o teste
    public void testObterConta() throws Exception {
        mockMvc.perform(get("/contas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.numeroConta").value("12345"))
                .andExpect(jsonPath("$.tipoConta").value("CORRENTE"))
                .andExpect(jsonPath("$.saldo").value(1000));
    }

    /**
     * Teste de integração que valida o comportamento do controller ao listar todas as contas.
     * @throws Exception
     */
    @Test
    public void testListarContas() throws Exception {
        mockMvc.perform(get("/contas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
