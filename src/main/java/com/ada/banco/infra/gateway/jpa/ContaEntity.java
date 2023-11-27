package com.ada.banco.infra.gateway.jpa;

import com.ada.banco.domain.model.enums.TipoConta;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity(name = "conta")
@EqualsAndHashCode(of = {"id", "idCliente"})
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "conta")
public class ContaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String numeroConta;
    @Enumerated(EnumType.STRING)
    private TipoConta tipoConta;

    private BigDecimal saldo;

    private Long idCliente;
}
