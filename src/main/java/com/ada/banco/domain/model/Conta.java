package com.ada.banco.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
public class Conta {
    private Long id;
    private Long agencia;
    private Long digito;
    private BigDecimal saldo; //mais preciso que double, melhor para grana

    //Usuario / Titular
    private String titular;
    private String cpf;
}
