package com.lobo.order.order;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    // Carrega os itens na mesma consulta, evitando lazy loading fora da transação
    @Override
    @EntityGraph(attributePaths = "items")
    Optional<Order> findById(UUID id);

}
