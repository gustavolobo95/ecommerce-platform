package com.lobo.order.order;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        OrderStatus status,
        BigDecimal total,
        Instant createAt,
        List<Item> items
) {

    public record Item(UUID productId, int quantity, BigDecimal unitPrice, BigDecimal subtotal) {

    }

    public static OrderResponse from(Order order) {
        List<Item> items = order.getItems().stream()
                .map(i -> new Item(i.getProductId(), i.getQuantity(), i.getUnitPrice(), i.subtotal()))
                .toList();
        return new OrderResponse(order.getId(), order.getStatus(), order.getTotal(),
                order.getCreatedAt(), items);
    }

}
