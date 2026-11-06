package rayyan.warehouse.workflowautomationengine.model;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "workflow_execution_steps")
public class WorkflowExecutionSteps {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "execution_id", nullable = false)
    private WorkflowExecutions execution;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "workflow_step_id", nullable = false)
    private WorkflowSteps workflowStep;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private WorkflowExecutionStepStatus status;

    @Column(name = "input_data", columnDefinition = "TEXT")
    private String inputData;

    @Column(name = "output_data", columnDefinition = "TEXT")
    private String outputData;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "started_at")
    private OffsetDateTime startedAt;

    @Column(name = "finished_at")
    private OffsetDateTime finishedAt;

    public WorkflowExecutionSteps() {}

    @PrePersist
    protected void onCreate() {
        if (status == null) {
            status = WorkflowExecutionStepStatus.PENDING;
        }
    }

    // Getters and setters

    public UUID getId() {
        return id;
    }

    public WorkflowExecutions getExecution() {
        return execution;
    }

    public void setExecution(WorkflowExecutions execution) {
        this.execution = execution;
    }

    public WorkflowSteps getWorkflowStep() {
        return workflowStep;
    }

    public void setWorkflowStep(WorkflowSteps workflowStep) {
        this.workflowStep = workflowStep;
    }

    public WorkflowExecutionStepStatus getStatus() {
        return status;
    }

    public void setStatus(WorkflowExecutionStepStatus status) {
        this.status = status;
    }

    public String getInputData() {
        return inputData;
    }

    public void setInputData(String inputData) {
        this.inputData = inputData;
    }

    public String getOutputData() {
        return outputData;
    }

    public void setOutputData(String outputData) {
        this.outputData = outputData;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
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
}