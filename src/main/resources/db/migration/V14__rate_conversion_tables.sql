CREATE TABLE currency_rate
(
  id             BIGINT PRIMARY KEY NOT NULL,
  currency       VARCHAR(3)         NOT NULL,
  last_update_on TIMESTAMP          NOT NULL
);

CREATE TABLE exchange_rate
(
  id               BIGINT PRIMARY KEY NOT NULL,
  currency_rate_id BIGINT             NOT NULL,
  date             DATE               NOT NULL,
  amount           NUMERIC            NOT NULL
);

CREATE SEQUENCE exchange_rate_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 3
CACHE 1;


CREATE SEQUENCE currency_rate_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 3
CACHE 1;

ALTER TABLE exchange_rate
  ADD FOREIGN KEY (currency_rate_id) REFERENCES currency_rate (id);
