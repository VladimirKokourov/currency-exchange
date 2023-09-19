DROP TABLE IF EXISTS currencies;
DROP TABLE IF EXISTS exchange_rates;

CREATE TABLE IF NOT EXISTS currencies
(
    id        integer PRIMARY KEY,
    code      varchar NOT NULL,
    full_name varchar NOT NULL,
    sign      varchar NOT NULL
);
CREATE UNIQUE INDEX idx_currencies_code
    ON currencies (code);

CREATE TABLE IF NOT EXISTS exchange_rates
(
    id                 integer PRIMARY KEY,
    base_currency_id   integer,
    target_currency_id integer,
    rate               decimal(6) NOT NULL,
    FOREIGN KEY (base_currency_id)
        REFERENCES currencies (id) ON DELETE CASCADE,
    FOREIGN KEY (target_currency_id)
        REFERENCES currencies (id) ON DELETE CASCADE
);

CREATE UNIQUE INDEX idx_base_target
    ON exchange_rates (base_currency_id, target_currency_id);