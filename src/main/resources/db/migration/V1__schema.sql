-- ==========================================
-- V1: Initial database schema
-- ==========================================

-- USERS
CREATE TABLE users (
                           id UUID PRIMARY KEY,
                           email VARCHAR(255) NOT NULL UNIQUE,
                           password_hash VARCHAR(255) NOT NULL,
                           created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);


-- WORKFLOWS
CREATE TABLE workflows (
                           id UUID PRIMARY KEY,
                           user_id UUID NOT NULL,
                           name VARCHAR(100) NOT NULL,
                           description TEXT,
                           enabled BOOLEAN NOT NULL DEFAULT FALSE,
                           created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                           CONSTRAINT fk_workflows_user
                               FOREIGN KEY (user_id)
                                   REFERENCES users(id)
                                   ON DELETE CASCADE
);


-- WORKFLOW STEPS
CREATE TABLE workflow_steps (
                                id UUID PRIMARY KEY,
                                workflow_id UUID NOT NULL,
                                name VARCHAR(100) NOT NULL,
                                step_type VARCHAR(20) NOT NULL,
                                step_order INTEGER NOT NULL,
                                configuration JSONB NOT NULL,
                                created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                CONSTRAINT fk_steps_workflow
                                    FOREIGN KEY (workflow_id)
                                        REFERENCES workflows(id)
                                        ON DELETE CASCADE,

                                CONSTRAINT chk_step_type
                                    CHECK (step_type IN ('HTTP', 'DELAY', 'CONDITIONAL')),

                                CONSTRAINT chk_step_order
                                    CHECK (step_order > 0),

                                CONSTRAINT uq_workflow_step_order
                                    UNIQUE (workflow_id, step_order)
);


-- ==========================================
-- INDEXES
-- ==========================================

CREATE INDEX idx_workflows_user_id
    ON workflows(user_id);

CREATE INDEX idx_workflow_steps_workflow_id
    ON workflow_steps(workflow_id);
