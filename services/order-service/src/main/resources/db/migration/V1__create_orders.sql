CREATE TABLE orders (
                        id         UUID          PRIMARY KEY,
                        status     VARCHAR(20)   NOT NULL,
                        total      NUMERIC(12,2) NOT NULL,
                        created_at TIMESTAMPTZ   NOT NULL DEFAULT now()
);

CREATE TABLE order_items (
                             id         UUID          PRIMARY KEY,
                             order_id   UUID          NOT NULL REFERENCES orders (id) ON DELETE CASCADE,
                             product_id UUID          NOT NULL,
                             quantity   INTEGER       NOT NULL CHECK (quantity > 0),
                             unit_price NUMERIC(12,2) NOT NULL CHECK (unit_price > 0)
);

CREATE INDEX idx_order_items_order_id ON order_items (order_id);