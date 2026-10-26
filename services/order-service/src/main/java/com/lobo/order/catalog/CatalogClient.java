package com.lobo.order.catalog;

import java.util.UUID;

public interface CatalogClient {

    /**
     * @throws ProductNotAvailableException se o produto não existe no catálogo
     * @throws CatalogUnavailableException  se o catálogo falhou, está fora do ar ou estourou o timeout
     */
    CatalogProduct getProduct(UUID productId);
}
