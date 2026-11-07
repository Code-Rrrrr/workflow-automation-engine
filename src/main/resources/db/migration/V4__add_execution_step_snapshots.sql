ALTER TABLE workflow_execution_steps
    ADD COLUMN step_name VARCHAR(100),
ADD COLUMN step_type VARCHAR(20),
ADD COLUMN step_order INTEGER,
ADD COLUMN configuration JSONB;