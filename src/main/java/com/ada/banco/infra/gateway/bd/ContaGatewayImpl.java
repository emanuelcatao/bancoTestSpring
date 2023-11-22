package com.ada.banco.infra.gateway.bd;

import com.ada.banco.domain.gateway.ContaGateway;
import com.ada.banco.domain.model.Conta;
import com.ada.banco.infra.gateway.jpa.ContaEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Transactional
public class ContaGatewayImpl implements ContaGateway {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Conta buscarPorCpf(String cpf) {
        try {
            // Primeiro, buscar o id do Cliente baseado no CPF
            Long idCliente = entityManager.createQuery(
                            "SELECT c.id FROM cliente c WHERE c.cpf = :cpf", Long.class)
                    .setParameter("cpf", cpf)
                    .getSingleResult();

            // Em seguida, buscar a Conta associada a esse Cliente
            ContaEntity contaEntity = entityManager.createQuery(
                            "SELECT c FROM conta c WHERE c.idCliente = :idCliente", ContaEntity.class)
                    .setParameter("idCliente", idCliente)
                    .getSingleResult();

            return toDomain(contaEntity);
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Conta salvar(Conta conta) {
        ContaEntity contaEntity = toEntity(conta);
        if (contaEntity.getId() == null) {
            entityManager.persist(contaEntity);
        } else {
            contaEntity = entityManager.merge(contaEntity);
        }
        return toDomain(contaEntity);
    }

    @Override
    public Conta obterContaPorId(Long idConta) {
        ContaEntity contaEntity = entityManager.find(ContaEntity.class, idConta);
        return contaEntity != null ? toDomain(contaEntity) : null;
    }

    @Override
    public Conta obterContaPorIdCliente(Long idCliente) {
        try {
            ContaEntity contaEntity = entityManager.createQuery(
                            "SELECT c FROM conta c WHERE c.idCliente = :idCliente", ContaEntity.class)
                    .setParameter("idCliente", idCliente)
                    .getSingleResult();
            return toDomain(contaEntity);
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Conta> listarTodas() {
        List<ContaEntity> contaEntities = entityManager.createQuery("SELECT c FROM conta c", ContaEntity.class)
                .getResultList();
        return contaEntities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }


    private Conta toDomain(ContaEntity contaEntity) {
        return new Conta(
                contaEntity.getId(),
                contaEntity.getNumeroConta(),
                contaEntity.getTipoConta(),
                contaEntity.getSaldo(),
                contaEntity.getIdCliente()
        );
    }

    private ContaEntity toEntity(Conta conta) {
        return new ContaEntity(
                conta.getId(),
                conta.getNumeroConta(),
                conta.getTipoConta(),
                conta.getSaldo(),
                conta.getIdCliente()
        );
    }
}
