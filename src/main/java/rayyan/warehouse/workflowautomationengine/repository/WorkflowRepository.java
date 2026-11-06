package rayyan.warehouse.workflowautomationengine.repository;

import org.hibernate.jdbc.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rayyan.warehouse.workflowautomationengine.model.Workflows;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkflowRepository extends JpaRepository<Workflows, UUID> {

    // fetch a particular workflow for a particular user
    Workflows findByIdAndUser_Id(UUID id, UUID userId);

    // fetch all the workflows for a user.
    List<Workflows> findAllByUser_Id(UUID id);

    // check whether a workflow exists for a user, without fetching it.
    boolean existsByIdAndUser_Id(UUID id, UUID userId);
}



