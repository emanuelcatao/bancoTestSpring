package com.ada.banco.infra.gateway.jpa;

import com.ada.banco.domain.model.TipoConta;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity(name = "conta")
@EqualsAndHashCode(of = {"id", "idCliente"})
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Table(name = "conta")
@ToString(of = {"id", "idCliente", "numeroConta", "tipoConta", "saldo"})
public class ContaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numeroConta;

    @Enumerated(EnumType.STRING)
    private TipoConta tipoConta;

    private BigDecimal saldo;

    private Long idCliente;
}
