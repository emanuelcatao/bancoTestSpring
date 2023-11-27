package com.ada.banco.infra.gateway.bd;

import com.ada.banco.domain.gateway.TransacaoGateway;
import com.ada.banco.domain.model.Conta;
import com.ada.banco.domain.model.Transacao;
import com.ada.banco.infra.gateway.jpa.TransacaoEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Transactional
public class TransacaoGatewayImpl implements TransacaoGateway {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Transacao registrarTransacao(Transacao transacao) {
        TransacaoEntity transacaoEntity = toEntity(transacao);
        entityManager.persist(transacaoEntity);
        return toDomain(transacaoEntity);
    }

    @Override
    public Transacao obterTransacaoPorId(Long id) {
        TransacaoEntity transacaoEntity = entityManager.find(TransacaoEntity.class, id);
        return toDomain(transacaoEntity);
    }

    @Override
    public List<Transacao> listarTodasAsTransacoesDaConta(Conta conta) {
        List<TransacaoEntity> transacoes = entityManager.createQuery(
                        "SELECT t FROM transacao t WHERE t.contaOrigemId = :contaId OR t.contaDestinoId = :contaId",
                        TransacaoEntity.class)
                .setParameter("contaId", conta.getId())
                .getResultList();

        return transacoes.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private TransacaoEntity toEntity(Transacao transacao) {
        return new TransacaoEntity(
                transacao.getId(),
                transacao.getTipo(),
                transacao.getValor(),
                transacao.getData(),
                transacao.getContaOrigemId(),
                transacao.getContaDestinoId()
        );
    }

    private Transacao toDomain(TransacaoEntity entity) {
        return new Transacao(
                entity.getId(),
                entity.getTipo(),
                entity.getValor(),
                entity.getData(),
                entity.getContaOrigemId(),
                entity.getContaDestinoId()
        );
    }
}