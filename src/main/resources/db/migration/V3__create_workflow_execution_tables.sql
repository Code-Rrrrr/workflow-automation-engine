CREATE TABLE workflow_executions (
                                     id UUID PRIMARY KEY,
                                     workflow_id UUID NOT NULL,
                                     status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
                                     started_at TIMESTAMPTZ,
                                     finished_at TIMESTAMPTZ,
                                     created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                     CONSTRAINT fk_execution_workflow
                                         FOREIGN KEY (workflow_id)
                                             REFERENCES workflows(id)
                                             ON DELETE CASCADE,

                                     CONSTRAINT chk_execution_status
                                         CHECK (status IN ('PENDING', 'RUNNING', 'COMPLETED', 'FAILED'))
);

CREATE INDEX idx_executions_workflow_id
    ON workflow_executions(workflow_id);

CREATE INDEX idx_executions_status
    ON workflow_executions(status);


CREATE TABLE workflow_execution_steps (
                                          id UUID PRIMARY KEY,
                                          execution_id UUID NOT NULL,
                                          workflow_step_id UUID NOT NULL,
                                          status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
                                          input_data TEXT,
                                          output_data TEXT,
                                          error_message TEXT,
                                          started_at TIMESTAMPTZ,
                                          finished_at TIMESTAMPTZ,

                                          CONSTRAINT fk_execution_steps_execution
                                              FOREIGN KEY (execution_id)
                                                  REFERENCES workflow_executions(id)
                                                  ON DELETE CASCADE,

                                          CONSTRAINT fk_execution_steps_workflow_step
                                              FOREIGN KEY (workflow_step_id)
                                                  REFERENCES workflow_steps(id),

                                          CONSTRAINT chk_execution_step_status
                                              CHECK (status IN ('PENDING', 'RUNNING', 'COMPLETED', 'FAILED', 'SKIPPED')),

                                          CONSTRAINT uq_execution_workflow_step
                                              UNIQUE (execution_id, workflow_step_id)
);

CREATE INDEX idx_execution_steps_execution_id
    ON workflow_execution_steps(execution_id);

CREATE INDEX idx_execution_steps_status
    ON workflow_execution_steps(status);