package com.ada.banco.infra.gateway.jpa;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "cliente")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "cpf")
public class ClienteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String nome;
    @Column(unique = true)
    private String cpf;
}