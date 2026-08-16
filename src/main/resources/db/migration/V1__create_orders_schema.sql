CREATE TABLE orders
(
    id          UUID PRIMARY KEY,
    customer_id VARCHAR(100)   NOT NULL,
    status      VARCHAR(20)    NOT NULL,
    total_price NUMERIC(12, 2) NOT NULL,
    created_at  TIMESTAMPTZ    NOT NULL
);

CREATE TABLE order_items
(
    id         BIGSERIAL PRIMARY KEY,
    order_id   UUID           NOT NULL REFERENCES orders (id),
    product_id VARCHAR(100)   NOT NULL,
    quantity   INTEGER        NOT NULL CHECK (quantity > 0),
    unit_price NUMERIC(12, 2) NOT NULL CHECK (unit_price >= 0)
);

CREATE INDEX idx_orders_customer_id ON orders (customer_id);
CREATE INDEX idx_order_items_order_id ON order_items (order_id);
