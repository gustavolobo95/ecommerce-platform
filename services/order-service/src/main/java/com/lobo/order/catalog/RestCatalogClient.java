package com.lobo.order.catalog;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestCatalogClient implements CatalogClient{

    private final RestClient catalogRestClient;

    @Override
    public CatalogProduct getProduct(UUID productId) {
        try {
            CatalogProduct product = catalogRestClient.get()
                    .uri("/products/{id}", productId)
                    .retrieve()
                    .onStatus(status -> status.value() == HttpStatus.NOT_FOUND.value(),
                            (request, response) -> {
                                throw new ProductNotAvailableException(productId);
                            })
                    .body(CatalogProduct.class);

            if (product == null) {
                throw new CatalogUnavailableException("Resposta vazia do catalog para " + productId, null);
            }
            return product;

        } catch (RestClientException e) {
            // 5xx, outros 4xx, timeout e falha de conexão: para o order é tudo "catalog indisponível"
            log.warn("Falha ao consultar o catalog para o produto {}: {}", productId, e.getMessage());
            throw new CatalogUnavailableException("Catalog indisponível ao consultar " + productId, e);
        }
    }

}
