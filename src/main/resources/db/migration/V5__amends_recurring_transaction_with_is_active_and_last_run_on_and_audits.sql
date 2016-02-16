ALTER TABLE recurring_transaction ADD COLUMN last_run_on DATE;
ALTER TABLE recurring_transaction ADD COLUMN is_active BOOLEAN NOT NULL DEFAULT TRUE;