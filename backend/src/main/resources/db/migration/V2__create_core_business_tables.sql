-- Core business tables for the Pricing IA MVP.
-- Schema "pricingia" is created in V1.

CREATE TABLE pricingia.merchant_settings (
    id                              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    shop_domain                     VARCHAR(255) NOT NULL,
    currency                        VARCHAR(8)   NOT NULL,
    min_margin_percentage           NUMERIC(6, 2) NOT NULL,
    max_price_increase_percentage   NUMERIC(6, 2) NOT NULL,
    automation_enabled              BOOLEAN      NOT NULL,
    created_at                      TIMESTAMPTZ  NOT NULL,
    updated_at                      TIMESTAMPTZ  NOT NULL,
    CONSTRAINT ux_merchant_settings_shop_domain UNIQUE (shop_domain)
);

CREATE TABLE pricingia.monitored_products (
    id                    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    merchant_settings_id  BIGINT       NOT NULL,
    shopify_product_id    VARCHAR(128) NOT NULL,
    title                 VARCHAR(512) NOT NULL,
    sku                   VARCHAR(255),
    current_price         NUMERIC(19, 4) NOT NULL,
    cost                  NUMERIC(19, 4) NOT NULL,
    inventory_quantity    INTEGER      NOT NULL DEFAULT 0,
    active                BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at            TIMESTAMPTZ  NOT NULL,
    updated_at            TIMESTAMPTZ  NOT NULL,
    CONSTRAINT fk_monitored_products_merchant
        FOREIGN KEY (merchant_settings_id) REFERENCES pricingia.merchant_settings (id)
);

CREATE INDEX ix_monitored_products_merchant
    ON pricingia.monitored_products (merchant_settings_id);

CREATE TABLE pricingia.shopify_webhook_events (
    id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    webhook_id    VARCHAR(128) NOT NULL,
    topic         VARCHAR(255) NOT NULL,
    shop_domain   VARCHAR(255) NOT NULL,
    payload       TEXT         NOT NULL,
    status        VARCHAR(32)  NOT NULL,
    received_at   TIMESTAMPTZ  NOT NULL,
    processed_at  TIMESTAMPTZ,
    CONSTRAINT ux_shopify_webhook_events_webhook_id UNIQUE (webhook_id)
);

CREATE TABLE pricingia.price_decisions (
    id                    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    merchant_settings_id  BIGINT        NOT NULL,
    monitored_product_id  BIGINT,
    old_price             NUMERIC(19, 4) NOT NULL,
    suggested_price       NUMERIC(19, 4) NOT NULL,
    reason                VARCHAR(1024) NOT NULL,
    status                VARCHAR(32)   NOT NULL,
    source_event_id       BIGINT,
    created_at            TIMESTAMPTZ   NOT NULL,
    updated_at            TIMESTAMPTZ   NOT NULL,
    CONSTRAINT fk_price_decisions_merchant
        FOREIGN KEY (merchant_settings_id) REFERENCES pricingia.merchant_settings (id),
    CONSTRAINT fk_price_decisions_product
        FOREIGN KEY (monitored_product_id) REFERENCES pricingia.monitored_products (id),
    CONSTRAINT fk_price_decisions_event
        FOREIGN KEY (source_event_id) REFERENCES pricingia.shopify_webhook_events (id)
);

CREATE INDEX ix_price_decisions_merchant
    ON pricingia.price_decisions (merchant_settings_id);

CREATE INDEX ix_price_decisions_status
    ON pricingia.price_decisions (status);
