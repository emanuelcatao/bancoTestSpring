package com.ada.banco.infra.gateway.bd;

import com.ada.banco.domain.gateway.ClienteGateway;
import com.ada.banco.domain.model.Cliente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

@Component
@Transactional
public class ClienteGatewayImpl implements ClienteGateway {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Cliente salvar(Cliente cliente) {
        if (cliente.getId() == null) {
            entityManager.persist(cliente);
        } else {
            cliente = entityManager.merge(cliente);
        }
        return cliente;
    }

    @Override
    public Cliente buscarPorCpf(String cpf) {
        return entityManager.createQuery("select c from Cliente c where c.cpf = :cpf", Cliente.class)
                .setParameter("cpf", cpf)
                .getSingleResult();
    }
}
