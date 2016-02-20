ALTER TABLE application_user DROP COLUMN first_name;
ALTER TABLE application_user DROP COLUMN last_name;
ALTER TABLE application_user ADD COLUMN email VARCHAR(500) DEFAULT '' NOT NULL;
ALTER TABLE application_user ADD COLUMN last_login_date DATE;

ALTER TABLE application_user ADD CONSTRAINT unique_email UNIQUE (email);