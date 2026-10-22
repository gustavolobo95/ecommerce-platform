CREATE TABLE products (
    id          UUID          PRIMARY KEY,
    name        VARCHAR(255)  NOT NULL,
    description VARCHAR(1000),
    price       NUMERIC(12,2) NOT NULL CHECK (price > 0),
    active      BOOLEAN       NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMPTZ   NOT NULL DEFAULT now()
);

CREATE INDEX idx_products_active ON products (active);

-- Dados iniciais para facilitar os primeiros testes
INSERT INTO products (id, name, description, price) VALUES
    (gen_random_uuid(), 'Teclado mecânico', 'Switch azul, layout ABNT2', 349.90),
    (gen_random_uuid(), 'Mouse sem fio', 'Ergonômico, 2400 DPI', 129.90),
    (gen_random_uuid(), 'Monitor 27"', 'IPS, 144Hz', 1899.00);
