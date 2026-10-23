package com.lobo.catalog.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        return repository.findByActiveTrue().stream()
                .map(ProductResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductResponse get(UUID id) {
        return repository.findByIdAndActiveTrue(id)
                .map(ProductResponse::from)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Transactional
    public ProductResponse create(ProductRequest request) {
        Product product = new Product(request.name(), request.description(), request.price());
        return ProductResponse.from(repository.save(product));
    }

    @Transactional
    public ProductResponse update(UUID id, ProductRequest request) {
        Product product = repository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        product.update(request.name(), request.description(), request.price());
        return ProductResponse.from(product);
    }

    @Transactional
    public void delete(UUID id) {
        Product product = repository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        product.deactivate(); // soft delete: pedidos antigos continuam referenciando o produto
    }
}
