package com.ada.banco.infra.gateway.bd;

import com.ada.banco.domain.gateway.ClienteGateway;
import com.ada.banco.domain.model.Cliente;
import com.ada.banco.domain.model.Conta;
import com.ada.banco.infra.gateway.jpa.ClienteEntity;
import com.ada.banco.infra.gateway.jpa.ContaEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Transactional
public class ClienteGatewayImpl implements ClienteGateway {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Cliente buscarPorCpf(String cpf) {
        try {
            ClienteEntity clienteEntity = entityManager.createQuery(
                            "SELECT c FROM cliente c WHERE c.cpf = :cpf", ClienteEntity.class)
                    .setParameter("cpf", cpf)
                    .getSingleResult();
            return toDomain(clienteEntity);
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Cliente salvar(Cliente cliente) {
        ClienteEntity clienteEntity = toEntity(cliente);

        try {
            if (clienteEntity.getId() == null) {
                entityManager.persist(clienteEntity);
            } else {
                clienteEntity = entityManager.merge(clienteEntity);
            }
        } catch (ConstraintViolationException e) {
            throw new IllegalArgumentException("Erro de validação: " +
                    e.getConstraintViolations().stream()
                    .map(cv -> cv.getMessage())
                    .collect(Collectors.joining(", "))
            );
        }

        return toDomain(clienteEntity);
    }

    @Override
    public Cliente buscarPorId(Long idCliente) {
        ClienteEntity clienteEntity = entityManager.find(ClienteEntity.class, idCliente);
        return clienteEntity != null ? toDomain(clienteEntity) : null;
    }

    @Override
    public List<Cliente> listarTodosOsClientes() {
        List<ClienteEntity> clienteEntities = entityManager.createQuery("SELECT c FROM cliente c", ClienteEntity.class)
                .getResultList();
        return clienteEntities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private Cliente toDomain(ClienteEntity clienteEntity) {
        return new Cliente(
                clienteEntity.getId(),
                clienteEntity.getNome(),
                clienteEntity.getCpf()
        );
    }

    private ClienteEntity toEntity(Cliente cliente) {
        return new ClienteEntity(
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpf()
        );
    }
}
