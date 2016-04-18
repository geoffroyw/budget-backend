CREATE TABLE recurring_transaction
(
  id                       BIGINT PRIMARY KEY NOT NULL,
  created_at               TIMESTAMP          NOT NULL,
  updated_at               TIMESTAMP          NOT NULL,
  amount_cents             INTEGER            NOT NULL,
  is_confirmed             BOOLEAN            NOT NULL,
  amount_currency          VARCHAR(255)       NOT NULL,
  description              VARCHAR(255)       NOT NULL,
  type                     VARCHAR(255)       NOT NULL,
  bank_account_id          BIGINT,
  payment_mean_id          BIGINT,
  date                     DATE               NOT NULL,
  temporal_expression_type VARCHAR(255)       NOT NULL,
  owner_id                 BIGINT             NOT NULL
);

CREATE TABLE recurring_transaction_category
(
  recurring_transaction_id BIGINT NOT NULL,
  category_id              BIGINT NOT NULL
);

ALTER TABLE recurring_transaction ADD FOREIGN KEY (owner_id) REFERENCES application_user (id);

CREATE SEQUENCE recurring_transaction_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 6
CACHE 1;
