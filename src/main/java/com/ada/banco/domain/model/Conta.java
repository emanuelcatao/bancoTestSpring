package com.ada.banco.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
public class Conta {
    private Long id;
    private String numeroConta;
    private TipoConta tipoConta;
    private BigDecimal saldo;
    private Long idCliente;
}