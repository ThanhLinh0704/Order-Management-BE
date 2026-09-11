-- ============================================================================
-- ORDER MANAGEMENT & TRACKING SYSTEM
-- Database Schema - Aligned with latest DBML baseline
-- Target DBMS: MySQL 8.0+
--
-- Tables in this baseline: 17
-- UUID policy: application generates UUID values and stores them as VARCHAR(36).
--
-- Mandatory columns in every table:
--   id, created_at, created_by, updated_at, updated_by, deleted
-- ============================================================================

CREATE DATABASE IF NOT EXISTS order_management_tracking
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE order_management_tracking;

-- ============================================================================
-- 1. ROLES
-- ============================================================================
CREATE TABLE roles (
    id          VARCHAR(36)  NOT NULL,
    code        VARCHAR(30)  NOT NULL,
    description VARCHAR(500) NULL,

    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by  VARCHAR(36)  NULL,
    updated_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by  VARCHAR(36)  NULL,
    deleted     TINYINT(1)   NOT NULL DEFAULT 0,

    CONSTRAINT pk_roles PRIMARY KEY (id),
    CONSTRAINT uk_roles_code UNIQUE (code),
    CONSTRAINT ck_roles_deleted CHECK (deleted IN (0, 1))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO roles (id, code, description, created_by, updated_by)
SELECT UUID(), 'ADMIN', 'Quản trị viên', 'SYSTEM', 'SYSTEM'
    WHERE NOT EXISTS (
    SELECT 1 FROM roles WHERE code = 'ADMIN'
);

INSERT INTO roles (id, code, description, created_by, updated_by)
SELECT UUID(), 'SHIPPER', 'Nhân viên giao hàng', 'SYSTEM', 'SYSTEM'
    WHERE NOT EXISTS (
    SELECT 1 FROM roles WHERE code = 'SHIPPER'
);

INSERT INTO roles (id, code, description, created_by, updated_by)
SELECT UUID(), 'CUSTOMER', 'Khách hàng', 'SYSTEM', 'SYSTEM'
    WHERE NOT EXISTS (
    SELECT 1 FROM roles WHERE code = 'CUSTOMER'
);

-- ============================================================================
-- 2. USERS
-- ============================================================================
CREATE TABLE users (
    id            VARCHAR(36)  NOT NULL,
    role_id       VARCHAR(36)  NOT NULL,
    email         VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name     VARCHAR(150) NOT NULL,
    phone         VARCHAR(30)  NULL,
    status        VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',

    created_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by    VARCHAR(36)  NULL,
    updated_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by    VARCHAR(36)  NULL,
    deleted       TINYINT(1)   NOT NULL DEFAULT 0,

    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uk_users_email UNIQUE (email),
    CONSTRAINT fk_users_role FOREIGN KEY (role_id)
        REFERENCES roles(id)
        ON UPDATE RESTRICT ON DELETE RESTRICT,
    CONSTRAINT ck_users_deleted CHECK (deleted IN (0, 1))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_users_role_id ON users(role_id);
CREATE INDEX idx_users_phone ON users(phone);
CREATE INDEX idx_users_status ON users(status);


-- ============================================================================
-- 3. USER ADDRESSES
-- ============================================================================
CREATE TABLE user_addresses (
    id              VARCHAR(36)  NOT NULL,
    user_id         VARCHAR(36)  NOT NULL,
    recipient_name  VARCHAR(150) NOT NULL,
    recipient_phone VARCHAR(30)  NOT NULL,
    address_line    VARCHAR(255) NOT NULL,
    ward            VARCHAR(120) NULL,
    district        VARCHAR(120) NULL,
    province        VARCHAR(120) NULL,
    is_default      TINYINT(1)   NOT NULL DEFAULT 0,

    created_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      VARCHAR(36)  NULL,
    updated_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by      VARCHAR(36)  NULL,
    deleted         TINYINT(1)   NOT NULL DEFAULT 0,

    CONSTRAINT pk_user_addresses PRIMARY KEY (id),
    CONSTRAINT fk_user_addresses_user FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON UPDATE RESTRICT ON DELETE RESTRICT,
    CONSTRAINT ck_user_addresses_default CHECK (is_default IN (0, 1)),
    CONSTRAINT ck_user_addresses_deleted CHECK (deleted IN (0, 1))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_user_addresses_user_id ON user_addresses(user_id);
CREATE INDEX idx_user_addresses_default ON user_addresses(user_id, is_default);


-- ============================================================================
-- 4. PRODUCTS
-- ============================================================================
CREATE TABLE products (
    id          VARCHAR(36)  NOT NULL,
    name        VARCHAR(255) NOT NULL,
    description TEXT         NULL,
    status      VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',

    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by  VARCHAR(36)  NULL,
    updated_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by  VARCHAR(36)  NULL,
    deleted     TINYINT(1)   NOT NULL DEFAULT 0,

    CONSTRAINT pk_products PRIMARY KEY (id),
    CONSTRAINT ck_products_deleted CHECK (deleted IN (0, 1))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_products_status_deleted ON products(status, deleted);
CREATE INDEX idx_products_name ON products(name);


-- ============================================================================
-- 5. PRODUCT VARIANTS
-- ============================================================================
CREATE TABLE product_variants (
    id           VARCHAR(36)   NOT NULL,
    product_id   VARCHAR(36)   NOT NULL,
    sku          VARCHAR(100)  NOT NULL,
    variant_name VARCHAR(255)  NOT NULL,
    attributes   JSON          NULL,
    price        DECIMAL(15,2) NOT NULL,
    status       VARCHAR(20)   NOT NULL DEFAULT 'ACTIVE',

    created_at   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by   VARCHAR(36)   NULL,
    updated_at   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by   VARCHAR(36)   NULL,
    deleted      TINYINT(1)    NOT NULL DEFAULT 0,

    CONSTRAINT pk_product_variants PRIMARY KEY (id),
    CONSTRAINT uk_product_variants_sku UNIQUE (sku),
    CONSTRAINT fk_product_variants_product FOREIGN KEY (product_id)
        REFERENCES products(id)
        ON UPDATE RESTRICT ON DELETE RESTRICT,
    CONSTRAINT ck_product_variants_price CHECK (price >= 0),
    CONSTRAINT ck_product_variants_deleted CHECK (deleted IN (0, 1))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_product_variants_product_id ON product_variants(product_id);
CREATE INDEX idx_product_variants_status_deleted ON product_variants(status, deleted);


-- ============================================================================
-- 6. INVENTORIES
-- ============================================================================
CREATE TABLE inventories (
    id          VARCHAR(36)  NOT NULL,
    code        VARCHAR(50)  NOT NULL,
    name        VARCHAR(150) NOT NULL,
    description VARCHAR(500) NULL,
    status      VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',

    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by  VARCHAR(36)  NULL,
    updated_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by  VARCHAR(36)  NULL,
    deleted     TINYINT(1)   NOT NULL DEFAULT 0,

    CONSTRAINT pk_inventories PRIMARY KEY (id),
    CONSTRAINT uk_inventories_code UNIQUE (code),
    CONSTRAINT ck_inventories_deleted CHECK (deleted IN (0, 1))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ============================================================================
-- 7. INVENTORY ITEMS
-- ============================================================================
CREATE TABLE inventory_items (
    id                 VARCHAR(36) NOT NULL,
    inventory_id       VARCHAR(36) NOT NULL,
    product_variant_id VARCHAR(36) NOT NULL,
    quantity_in_stock  INT         NOT NULL DEFAULT 0,

    created_at         DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by         VARCHAR(36) NULL,
    updated_at         DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by         VARCHAR(36) NULL,
    deleted            TINYINT(1)  NOT NULL DEFAULT 0,

    CONSTRAINT pk_inventory_items PRIMARY KEY (id),
    CONSTRAINT uk_inventory_items_inventory_variant UNIQUE (inventory_id, product_variant_id),
    CONSTRAINT fk_inventory_items_inventory FOREIGN KEY (inventory_id)
        REFERENCES inventories(id)
        ON UPDATE RESTRICT ON DELETE RESTRICT,
    CONSTRAINT fk_inventory_items_variant FOREIGN KEY (product_variant_id)
        REFERENCES product_variants(id)
        ON UPDATE RESTRICT ON DELETE RESTRICT,
    CONSTRAINT ck_inventory_items_quantity CHECK (quantity_in_stock >= 0),
    CONSTRAINT ck_inventory_items_deleted CHECK (deleted IN (0, 1))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_inventory_items_inventory_id ON inventory_items(inventory_id);
CREATE INDEX idx_inventory_items_variant_id ON inventory_items(product_variant_id);


-- ============================================================================
-- 8. CARTS
-- User 1 -> 0..1 Cart, enforced by UNIQUE(user_id).
-- ============================================================================
CREATE TABLE carts (
    id          VARCHAR(36) NOT NULL,
    user_id     VARCHAR(36) NOT NULL,

    created_at  DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by  VARCHAR(36) NULL,
    updated_at  DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by  VARCHAR(36) NULL,
    deleted     TINYINT(1)  NOT NULL DEFAULT 0,

    CONSTRAINT pk_carts PRIMARY KEY (id),
    CONSTRAINT uk_carts_user_id UNIQUE (user_id),
    CONSTRAINT fk_carts_user FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON UPDATE RESTRICT ON DELETE RESTRICT,
    CONSTRAINT ck_carts_deleted CHECK (deleted IN (0, 1))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ============================================================================
-- 9. CART ITEMS
-- ============================================================================
CREATE TABLE cart_items (
    id                 VARCHAR(36) NOT NULL,
    cart_id            VARCHAR(36) NOT NULL,
    product_variant_id VARCHAR(36) NOT NULL,
    quantity           INT         NOT NULL,

    created_at         DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by         VARCHAR(36) NULL,
    updated_at         DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by         VARCHAR(36) NULL,
    deleted            TINYINT(1)  NOT NULL DEFAULT 0,

    CONSTRAINT pk_cart_items PRIMARY KEY (id),
    CONSTRAINT uk_cart_items_cart_variant UNIQUE (cart_id, product_variant_id),
    CONSTRAINT fk_cart_items_cart FOREIGN KEY (cart_id)
        REFERENCES carts(id)
        ON UPDATE RESTRICT ON DELETE RESTRICT,
    CONSTRAINT fk_cart_items_variant FOREIGN KEY (product_variant_id)
        REFERENCES product_variants(id)
        ON UPDATE RESTRICT ON DELETE RESTRICT,
    CONSTRAINT ck_cart_items_quantity CHECK (quantity > 0),
    CONSTRAINT ck_cart_items_deleted CHECK (deleted IN (0, 1))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_cart_items_cart_id ON cart_items(cart_id);
CREATE INDEX idx_cart_items_variant_id ON cart_items(product_variant_id);


-- ============================================================================
-- 12. ORDERS
-- carrier_id is retained exactly as provided in the DBML. No carriers table or
-- foreign-key relationship is defined in this baseline.
-- ============================================================================
CREATE TABLE orders (
    id                      VARCHAR(36)   NOT NULL,
    customer_id             VARCHAR(36)   NOT NULL,
    carrier_id              VARCHAR(36)   NULL,
    tracking_number         VARCHAR(50)   NOT NULL,
    status                  VARCHAR(20)   NOT NULL,
    subtotal_amount         DECIMAL(15,2) NOT NULL DEFAULT 0,
    discount_amount         DECIMAL(15,2) NOT NULL DEFAULT 0,
    shipping_fee            DECIMAL(15,2) NOT NULL DEFAULT 0,
    total_amount            DECIMAL(15,2) NOT NULL DEFAULT 0,
    estimated_delivery_date DATE          NULL,

    created_at              DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by              VARCHAR(36)   NULL,
    updated_at              DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by              VARCHAR(36)   NULL,
    deleted                 TINYINT(1)    NOT NULL DEFAULT 0,

    CONSTRAINT pk_orders PRIMARY KEY (id),
    CONSTRAINT uk_orders_tracking_number UNIQUE (tracking_number),
    CONSTRAINT fk_orders_customer FOREIGN KEY (customer_id)
        REFERENCES users(id)
        ON UPDATE RESTRICT ON DELETE RESTRICT,
    CONSTRAINT ck_orders_subtotal CHECK (subtotal_amount >= 0),
    CONSTRAINT ck_orders_discount CHECK (discount_amount >= 0),
    CONSTRAINT ck_orders_shipping_fee CHECK (shipping_fee >= 0),
    CONSTRAINT ck_orders_total CHECK (total_amount >= 0),
    CONSTRAINT ck_orders_deleted CHECK (deleted IN (0, 1))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_orders_customer_id ON orders(customer_id);
CREATE INDEX idx_orders_carrier_id ON orders(carrier_id);
CREATE INDEX idx_orders_created_at ON orders(created_at);
CREATE INDEX idx_orders_status_deleted ON orders(status, deleted);
CREATE INDEX idx_orders_customer_status_deleted ON orders(customer_id, status, deleted);


-- ============================================================================
-- 13. ORDER ITEMS
-- Historical snapshot of purchased Product Variant.
-- ============================================================================
CREATE TABLE order_items (
    id                    VARCHAR(36)   NOT NULL,
    order_id              VARCHAR(36)   NOT NULL,
    product_variant_id    VARCHAR(36)   NULL,
    sku_snapshot          VARCHAR(100)  NOT NULL,
    variant_name_snapshot VARCHAR(255)  NOT NULL,
    unit_price            DECIMAL(15,2) NOT NULL,
    quantity              INT           NOT NULL,
    discount_amount       DECIMAL(15,2) NOT NULL DEFAULT 0,
    price_total           DECIMAL(15,2) NOT NULL,

    created_at            DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by            VARCHAR(36)   NULL,
    updated_at            DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by            VARCHAR(36)   NULL,
    deleted               TINYINT(1)    NOT NULL DEFAULT 0,

    CONSTRAINT pk_order_items PRIMARY KEY (id),
    CONSTRAINT fk_order_items_order FOREIGN KEY (order_id)
        REFERENCES orders(id)
        ON UPDATE RESTRICT ON DELETE RESTRICT,
    CONSTRAINT fk_order_items_variant FOREIGN KEY (product_variant_id)
        REFERENCES product_variants(id)
        ON UPDATE RESTRICT ON DELETE RESTRICT,
    CONSTRAINT ck_order_items_unit_price CHECK (unit_price >= 0),
    CONSTRAINT ck_order_items_quantity CHECK (quantity > 0),
    CONSTRAINT ck_order_items_discount CHECK (discount_amount >= 0),
    CONSTRAINT ck_order_items_price_total CHECK (price_total >= 0),
    CONSTRAINT ck_order_items_deleted CHECK (deleted IN (0, 1))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_order_items_order_id ON order_items(order_id);
CREATE INDEX idx_order_items_variant_id ON order_items(product_variant_id);


-- ============================================================================
-- 14. ORDER ADDRESSES
-- Order 1 -> 1 Order Address, enforced by UNIQUE(order_id).
-- ============================================================================
CREATE TABLE order_addresses (
    id              VARCHAR(36)  NOT NULL,
    order_id        VARCHAR(36)  NOT NULL,
    recipient_name  VARCHAR(150) NOT NULL,
    recipient_phone VARCHAR(30)  NOT NULL,
    address_line    VARCHAR(255) NOT NULL,
    ward            VARCHAR(120) NULL,
    district        VARCHAR(120) NULL,
    province        VARCHAR(120) NULL,

    created_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      VARCHAR(36)  NULL,
    updated_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by      VARCHAR(36)  NULL,
    deleted         TINYINT(1)   NOT NULL DEFAULT 0,

    CONSTRAINT pk_order_addresses PRIMARY KEY (id),
    CONSTRAINT uk_order_addresses_order_id UNIQUE (order_id),
    CONSTRAINT fk_order_addresses_order FOREIGN KEY (order_id)
        REFERENCES orders(id)
        ON UPDATE RESTRICT ON DELETE RESTRICT,
    CONSTRAINT ck_order_addresses_deleted CHECK (deleted IN (0, 1))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ============================================================================
-- 15. PAYMENTS
-- ============================================================================
CREATE TABLE payments (
    id             VARCHAR(36)   NOT NULL,
    order_id       VARCHAR(36)   NULL,
    payment_method VARCHAR(20)   NOT NULL,
    status         VARCHAR(20)   NOT NULL DEFAULT 'PENDING',
    amount         DECIMAL(15,2) NOT NULL,
    paid_at        DATETIME      NULL,

    created_at     DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by     VARCHAR(36)   NULL,
    updated_at     DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by     VARCHAR(36)   NULL,
    deleted        TINYINT(1)    NOT NULL DEFAULT 0,

    CONSTRAINT pk_payments PRIMARY KEY (id),
    CONSTRAINT fk_payments_order FOREIGN KEY (order_id)
        REFERENCES orders(id)
        ON UPDATE RESTRICT ON DELETE RESTRICT,
    CONSTRAINT ck_payments_amount CHECK (amount >= 0),
    CONSTRAINT ck_payments_deleted CHECK (deleted IN (0, 1))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_payments_order_id ON payments(order_id);
CREATE INDEX idx_payments_status_deleted ON payments(status, deleted);


-- ============================================================================
-- 16. SHIPPER ASSIGNMENTS
-- The provided DBML defines Order 1 -> N Shipper Assignments because order_id is
-- a normal foreign key and is not UNIQUE.
-- ============================================================================
CREATE TABLE shipper_assignments (
    id          VARCHAR(36) NOT NULL,
    order_id    VARCHAR(36) NOT NULL,
    shipper_id  VARCHAR(36) NOT NULL,
    status      VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    assigned_at DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    created_at  DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by  VARCHAR(36) NULL,
    updated_at  DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by  VARCHAR(36) NULL,
    deleted     TINYINT(1)  NOT NULL DEFAULT 0,

    CONSTRAINT pk_shipper_assignments PRIMARY KEY (id),
    CONSTRAINT fk_shipper_assignments_order FOREIGN KEY (order_id)
        REFERENCES orders(id)
        ON UPDATE RESTRICT ON DELETE RESTRICT,
    CONSTRAINT fk_shipper_assignments_shipper FOREIGN KEY (shipper_id)
        REFERENCES users(id)
        ON UPDATE RESTRICT ON DELETE RESTRICT,
    CONSTRAINT ck_shipper_assignments_deleted CHECK (deleted IN (0, 1))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_shipper_assignments_order_id ON shipper_assignments(order_id);
CREATE INDEX idx_shipper_assignments_shipper_id ON shipper_assignments(shipper_id);
CREATE INDEX idx_shipper_assignments_order_status_deleted
    ON shipper_assignments(order_id, status, deleted);


-- ============================================================================
-- 17. TRACKING LOGS
-- ============================================================================
CREATE TABLE tracking_logs (
    id          VARCHAR(36)  NOT NULL,
    order_id    VARCHAR(36)  NOT NULL,
    from_status VARCHAR(20)  NULL,
    to_status   VARCHAR(20)  NOT NULL,
    note        VARCHAR(500) NULL,

    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by  VARCHAR(36)  NULL,
    updated_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by  VARCHAR(36)  NULL,
    deleted     TINYINT(1)   NOT NULL DEFAULT 0,

    CONSTRAINT pk_tracking_logs PRIMARY KEY (id),
    CONSTRAINT fk_tracking_logs_order FOREIGN KEY (order_id)
        REFERENCES orders(id)
        ON UPDATE RESTRICT ON DELETE RESTRICT,
    CONSTRAINT ck_tracking_logs_deleted CHECK (deleted IN (0, 1))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_tracking_logs_order_created_at ON tracking_logs(order_id, created_at);
CREATE INDEX idx_tracking_logs_to_status ON tracking_logs(to_status);


-- ============================================================================
-- 18. DELIVERY ATTEMPTS
-- ============================================================================
CREATE TABLE delivery_attempts (
    id             VARCHAR(36)  NOT NULL,
    order_id       VARCHAR(36)  NOT NULL,
    shipper_id     VARCHAR(36)  NOT NULL,
    attempt_no     INT          NOT NULL,
    status         VARCHAR(20)  NOT NULL DEFAULT 'IN_PROGRESS',
    started_at     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_at   DATETIME     NULL,
    failure_reason VARCHAR(500) NULL,

    created_at     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by     VARCHAR(36)  NULL,
    updated_at     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by     VARCHAR(36)  NULL,
    deleted        TINYINT(1)   NOT NULL DEFAULT 0,

    CONSTRAINT pk_delivery_attempts PRIMARY KEY (id),
    CONSTRAINT uk_delivery_attempts_order_attempt UNIQUE (order_id, attempt_no),
    CONSTRAINT fk_delivery_attempts_order FOREIGN KEY (order_id)
        REFERENCES orders(id)
        ON UPDATE RESTRICT ON DELETE RESTRICT,
    CONSTRAINT fk_delivery_attempts_shipper FOREIGN KEY (shipper_id)
        REFERENCES users(id)
        ON UPDATE RESTRICT ON DELETE RESTRICT,
    CONSTRAINT ck_delivery_attempts_attempt_no CHECK (attempt_no > 0),
    CONSTRAINT ck_delivery_attempts_deleted CHECK (deleted IN (0, 1))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_delivery_attempts_order_id ON delivery_attempts(order_id);
CREATE INDEX idx_delivery_attempts_shipper_id ON delivery_attempts(shipper_id);
CREATE INDEX idx_delivery_attempts_status_deleted ON delivery_attempts(status, deleted);


-- ============================================================================
-- 19. NOTIFICATIONS
-- Recipient is resolved through orders.customer_id; no user_id column exists in
-- the provided DBML baseline.
-- ============================================================================
CREATE TABLE notifications (
    id              VARCHAR(36)  NOT NULL,
    order_id        VARCHAR(36)  NOT NULL,
    event_type      VARCHAR(30)  NOT NULL,
    title           VARCHAR(255) NOT NULL,
    message         TEXT         NOT NULL,
    delivery_status VARCHAR(20)  NOT NULL DEFAULT 'PENDING',
    scheduled_at    DATETIME     NULL,
    sent_at         DATETIME     NULL,

    created_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      VARCHAR(36)  NULL,
    updated_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by      VARCHAR(36)  NULL,
    deleted         TINYINT(1)   NOT NULL DEFAULT 0,

    CONSTRAINT pk_notifications PRIMARY KEY (id),
    CONSTRAINT fk_notifications_order FOREIGN KEY (order_id)
        REFERENCES orders(id)
        ON UPDATE RESTRICT ON DELETE RESTRICT,
    CONSTRAINT ck_notifications_deleted CHECK (deleted IN (0, 1))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_notifications_order_id ON notifications(order_id);
CREATE INDEX idx_notifications_delivery_schedule_deleted
    ON notifications(delivery_status, scheduled_at, deleted);


-- ============================================================================
-- RELATIONSHIP SUMMARY
-- ============================================================================
-- roles              1 -> N users
-- users              1 -> N user_addresses
-- users              1 -> 0..1 carts
-- products           1 -> N product_variants
-- inventories        1 -> N inventory_items
-- product_variants   1 -> N inventory_items
-- carts              1 -> N cart_items
-- product_variants   1 -> N cart_items
-- users(Customer)    1 -> N orders
-- orders             1 -> N order_items
-- product_variants   1 -> N order_items
-- orders             1 -> 1 order_addresses
-- orders             1 -> N payments
-- orders             1 -> N shipper_assignments
-- users(Shipper)     1 -> N shipper_assignments
-- orders             1 -> N tracking_logs
-- orders             1 -> N delivery_attempts
-- users(Shipper)     1 -> N delivery_attempts
-- orders             1 -> N notifications
--
-- carrier_id exists on orders, but the latest DBML does not define a carriers
-- table or a foreign-key relationship for it.
-- ============================================================================
