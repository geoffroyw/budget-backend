ALTER TABLE bank_accounts ADD owner_id BIGINT NOT NULL;
ALTER TABLE bank_accounts ADD FOREIGN KEY (owner_id) REFERENCES application_user (id);

ALTER TABLE payment_mean ADD owner_id BIGINT NOT NULL;
ALTER TABLE payment_mean ADD FOREIGN KEY (owner_id) REFERENCES application_user (id);

ALTER TABLE transaction ADD owner_id BIGINT NOT NULL;
ALTER TABLE transaction ADD FOREIGN KEY (owner_id) REFERENCES application_user (id);