CREATE TABLE application_user
(
  id         BIGINT PRIMARY KEY NOT NULL,
  first_name VARCHAR(255)       NOT NULL,
  last_name  VARCHAR(255)       NOT NULL,
  login      VARCHAR(255)       NOT NULL,
  password   VARCHAR(255)       NOT NULL
);
CREATE TABLE bank_accounts
(
  id         BIGINT PRIMARY KEY NOT NULL,
  created_at TIMESTAMP          NOT NULL,
  updated_at TIMESTAMP          NOT NULL,
  currency   VARCHAR(255)       NOT NULL,
  name       VARCHAR(255)       NOT NULL
);
CREATE TABLE category
(
  id         BIGINT PRIMARY KEY NOT NULL,
  created_at TIMESTAMP          NOT NULL,
  updated_at TIMESTAMP          NOT NULL,
  name       VARCHAR(255)       NOT NULL
);
CREATE TABLE payment_mean
(
  id         BIGINT PRIMARY KEY NOT NULL,
  created_at TIMESTAMP          NOT NULL,
  updated_at TIMESTAMP          NOT NULL,
  currency   VARCHAR(255)       NOT NULL,
  name       VARCHAR(255)       NOT NULL
);
CREATE TABLE role
(
  id   BIGINT PRIMARY KEY NOT NULL,
  name VARCHAR(255)       NOT NULL
);
CREATE TABLE transaction
(
  id              BIGINT PRIMARY KEY NOT NULL,
  created_at      TIMESTAMP          NOT NULL,
  updated_at      TIMESTAMP          NOT NULL,
  amount_cents    INTEGER            NOT NULL,
  is_confirmed    BOOLEAN            NOT NULL,
  amount_currency VARCHAR(255)       NOT NULL,
  description     VARCHAR(255)       NOT NULL,
  type            VARCHAR(255)       NOT NULL,
  bank_account_id BIGINT,
  payment_mean_id BIGINT,
  date            DATE               NOT NULL
);
CREATE TABLE transaction_category
(
  transaction_id BIGINT NOT NULL,
  category_id    BIGINT NOT NULL
);
CREATE TABLE user_role
(
  user_id BIGINT NOT NULL,
  role_id BIGINT NOT NULL,
  CONSTRAINT user_role_pkey PRIMARY KEY (user_id, role_id)
);
CREATE UNIQUE INDEX uk_43csyr6i5h9r1medgmoho6nqh ON application_user (login);
ALTER TABLE transaction ADD FOREIGN KEY (bank_account_id) REFERENCES bank_accounts (id);
ALTER TABLE transaction ADD FOREIGN KEY (payment_mean_id) REFERENCES payment_mean (id);
ALTER TABLE transaction_category ADD FOREIGN KEY (transaction_id) REFERENCES transaction (id);
ALTER TABLE transaction_category ADD FOREIGN KEY (category_id) REFERENCES category (id);
ALTER TABLE user_role ADD FOREIGN KEY (user_id) REFERENCES application_user (id);
ALTER TABLE user_role ADD FOREIGN KEY (role_id) REFERENCES role (id);


CREATE SEQUENCE category_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 4
CACHE 1;


CREATE SEQUENCE bank_account_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 2
CACHE 1;


CREATE SEQUENCE payment_mean_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 5
CACHE 1;


CREATE SEQUENCE role_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 3
CACHE 1;


CREATE SEQUENCE transaction_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 5
CACHE 1;


CREATE SEQUENCE user_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 6
CACHE 1;
