ALTER TABLE category ADD owner_id BIGINT NOT NULL;
ALTER TABLE category ADD FOREIGN KEY (owner_id) REFERENCES application_user (id);