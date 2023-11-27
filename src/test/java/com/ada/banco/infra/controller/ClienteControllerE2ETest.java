package com.ada.banco.infra.controller;

import com.ada.banco.domain.model.Cliente;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations="classpath:application-test.properties")
public class ClienteControllerE2ETest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void deveCriarEListarCliente() {
        Cliente novoCliente = new Cliente(null, "E2E Cliente", "448.046.050-02");
        String baseUrl = "http://localhost:" + port + "/clientes";

        ResponseEntity<String> respostaCriacao = restTemplate.postForEntity(baseUrl, novoCliente, String.class);
        if (respostaCriacao.getStatusCode() != HttpStatus.CREATED) {
            System.out.println("Erro ao criar cliente: " + respostaCriacao.getBody());
        }
        Assertions.assertEquals(HttpStatus.CREATED, respostaCriacao.getStatusCode());

        ResponseEntity<Cliente[]> respostaListagem = restTemplate.getForEntity(baseUrl, Cliente[].class);
        Assertions.assertEquals(HttpStatus.OK, respostaListagem.getStatusCode());
        Assertions.assertNotNull(respostaListagem.getBody());
        Assertions.assertTrue(respostaListagem.getBody().length > 0);
    }

    @Test
    public void deveLancarExceptionSeCPFInvalido() {
        Cliente novoCliente = new Cliente(null, "E2E Cliente", "448.046.050-01");
        String baseUrl = "http://localhost:" + port + "/clientes";

        ResponseEntity<String> respostaCriacao = restTemplate.postForEntity(baseUrl, novoCliente, String.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, respostaCriacao.getStatusCode());
        Assertions.assertTrue(respostaCriacao.getBody().contains("CPF inválido"));
    }
}