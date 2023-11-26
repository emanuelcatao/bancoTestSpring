package com.ada.banco.infra.gateway.jpa;

import com.ada.banco.domain.model.TipoTransacao;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "transacao")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "tipo", "valor", "data", "contaOrigemId", "contaDestinoId"})
public class TransacaoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private TipoTransacao tipo;
    private BigDecimal valor;
    @Column(name = "data_transacao")
    private LocalDateTime data;
    private Long contaOrigemId;
    private Long contaDestinoId;
}

