DELETE
FROM currencies;
DELETE
FROM exchange_rates;

INSERT INTO currencies (code, full_name, sign)
VALUES ('RUB', 'Russian Rubles', '₽'),
       ('EUR', 'Euros', '€'),
       ('USD', 'US Dollars', '$'),
       ('GBP', 'British Pounds', '£'),
       ('CNY', 'Chinese Yuan', '¥');

INSERT INTO exchange_rates (base_currency_id, target_currency_id, rate)
VALUES (1, 2, 0.009687),
       (1, 3, 0.010335),
       (1, 4, 0.008341),
       (1, 5, 0.075154),
       (2, 1, 103.22332),
       (2, 3, 1.066909),
       (3, 4, 0.936891),
       (2, 5, 1.066799);