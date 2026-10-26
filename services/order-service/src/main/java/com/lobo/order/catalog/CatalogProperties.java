package com.lobo.order.catalog;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "catalog")
public record CatalogProperties(
        String baseUrl,
        Duration connectTimeout,
        Duration readTimeout
) {
}
