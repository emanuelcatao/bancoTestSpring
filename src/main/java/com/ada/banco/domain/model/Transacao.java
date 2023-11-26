package com.ada.banco.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Transacao {
    private Long id;
    private TipoTransacao tipo;
    private BigDecimal valor;
    private LocalDateTime data;
    private Long contaOrigemId;
    private Long contaDestinoId;
}

