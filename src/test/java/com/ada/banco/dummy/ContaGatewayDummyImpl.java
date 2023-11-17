package com.ada.banco.dummy;

import com.ada.banco.domain.gateway.ContaGateway;
import com.ada.banco.domain.model.Conta;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ContaGatewayDummyImpl implements ContaGateway {
    private Map<String, Conta> contas = new HashMap<>();

    public ContaGatewayDummyImpl() {
        contas.put("12312312312", new Conta(1L, 0002L, 1L, BigDecimal.ZERO, "Enzo Kawaii", "12312312312"));
        contas.put("12345678900", new Conta(101L, 0001L, 1234L, new BigDecimal("1000.00"), "Alice", "12345678900"));
        contas.put("12312445212", new Conta(1231L, 0002L, 1L, BigDecimal.valueOf(10000), "Ligia", "12312445212"));
        contas.put("98765432100", new Conta(102L, 0001L, 1235L, new BigDecimal("2000.00"), "Bob", "98765432100"));
    }

    @Override
    public Conta buscarPorCpf(String cpf) {
        return contas.get(cpf);
    }

    @Override
    public Conta salvar(Conta conta) {
        contas.put(conta.getCpf(), conta);
        return conta;
    }

    @Override
    public Conta obterContaPorId(Long idConta) {
        for (Conta conta : contas.values()) {
            if (conta.getId().equals(idConta)) {
                return conta;
            }
        }
        return null;
    }
}
