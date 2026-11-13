package rayyan.warehouse.workflowautomationengine.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "workflow_executions")
public class WorkflowExecutions {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "workflow_id", nullable = false)
    private Workflows workflow;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private WorkflowExecutionStatus status;

    @Column(name = "started_at")
    private OffsetDateTime startedAt;

    @Column(name = "finished_at")
    private OffsetDateTime finishedAt;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "step_name", length = 100)
    private String stepName;

    @Enumerated(EnumType.STRING)
    @Column(name = "step_type", length = 20)
    private StepType stepType;

    @Column(name = "step_order")
    private Integer stepOrder;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "configuration", columnDefinition = "jsonb")
    private String configuration;

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public StepType getStepType() {
        return stepType;
    }

    public void setStepType(StepType stepType) {
        this.stepType = stepType;
    }

    public Integer getStepOrder() {
        return stepOrder;
    }

    public void setStepOrder(Integer stepOrder) {
        this.stepOrder = stepOrder;
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public WorkflowExecutions() {}

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();

        if (status == null) {
            status = WorkflowExecutionStatus.PENDING;
        }
    }

    // Getters and setters

    public UUID getId() {
        return id;
    }

    public Workflows getWorkflow() {
        return workflow;
    }

    public void setWorkflow(Workflows workflow) {
        this.workflow = workflow;
    }

    public WorkflowExecutionStatus getStatus() {
        return status;
    }

    public void setStatus(WorkflowExecutionStatus status) {
        this.status = status;
    }

    public OffsetDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(OffsetDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public OffsetDateTime getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(OffsetDateTime finishedAt) {
        this.finishedAt = finishedAt;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}