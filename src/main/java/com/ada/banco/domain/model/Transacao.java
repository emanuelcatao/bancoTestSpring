package com.ada.banco.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class Transacao {
    private Long idTransacao;
    private Long idConta;
    private TipoTransacao tipo;
    private BigDecimal valor;
    private LocalDateTime dataTransacao;
}

