package com.ada.banco.domain.usecase;

import com.ada.banco.domain.gateway.ContaGateway;
import com.ada.banco.domain.model.Conta;
import com.ada.banco.dummy.ContaGatewayDummyImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class CriarNovaContaTest {
    private CriarNovaConta criarNovaConta;

    @BeforeEach
    public void setUp() {
        ContaGateway contaGateway = new ContaGatewayDummyImpl();
        this.criarNovaConta = new CriarNovaConta(contaGateway);
    }

    @Test
    public void deveLancarExceptionQuandoUsuarioJaPossuirConta() {
        Conta conta = new Conta(1L, 0002L, 1L, BigDecimal.ZERO, "Enzo Kawaii", "12312312312");
        Assertions.assertThrows(Exception.class, () -> criarNovaConta.execute(conta));
    }

    @Test
    public void deveCriarUmaNovaContaComSucesso() throws Exception {
        Conta conta = new Conta(1231L, 0002L, 1L, BigDecimal.valueOf(10000), "Ligia", "123124452555");
        Conta contaCriada = criarNovaConta.execute(conta);
        Assertions.assertAll(
                () -> Assertions.assertEquals("Ligia", contaCriada.getTitular()),
                () -> Assertions.assertEquals("123124452555", contaCriada.getCpf()),
                () -> Assertions.assertEquals(BigDecimal.valueOf(10000), contaCriada.getSaldo())
        );
        Assertions.assertNotNull(contaCriada);
    }
}
