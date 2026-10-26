package com.lobo.order.catalog;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.UUID;

// Tolerant reader: campos novos no catalog não quebram o order
@JsonIgnoreProperties(ignoreUnknown = true)
public record CatalogProduct(UUID id, String name, BigDecimal price) {
}
