package io.miragon.bpmrepo.core.repository.infrastructure.repository;

import io.miragon.bpmrepo.core.repository.infrastructure.entity.AssignmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssignmentJpa extends JpaRepository<AssignmentEntity, String> {

    List<AssignmentEntity> findAssignmentEntitiesByAssignmentId_UserIdEquals(String userId);

    AssignmentEntity findByAssignmentId_BpmnRepositoryIdAndAssignmentId_UserId(String bpmnRepositoryId, String userId);

    List<AssignmentEntity> findByAssignmentId_BpmnRepositoryId(String bpmnRepositoryId);

    int countByAssignmentId_BpmnRepositoryId(String bpmnRepositoryId);

    void deleteAssignmentEntityByAssignmentId_BpmnRepositoryIdAndAssignmentId_UserId(String bpmnRepositoryId, String userId);

    int deleteAllByAssignmentId_BpmnRepositoryId(String bpmnRepositoryId);

}
