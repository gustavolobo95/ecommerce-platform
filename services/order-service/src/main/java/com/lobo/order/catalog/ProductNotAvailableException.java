package com.lobo.order.catalog;

import java.util.UUID;

public class ProductNotAvailableException extends RuntimeException {
    public ProductNotAvailableException(UUID productId) {
        super("Produto inexistente ou indisponível: " + productId);
    }
}
