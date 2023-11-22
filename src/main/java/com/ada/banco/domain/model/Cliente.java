package com.ada.banco.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
public class Cliente {
    private Long id;
    private String nome;
    private String cpf;
}