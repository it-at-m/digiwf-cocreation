package de.muenchen.oss.digiwf.cocreation.core.repository.infrastructure.repository;

import de.muenchen.oss.digiwf.cocreation.core.repository.infrastructure.entity.AssignmentEntity;
import de.muenchen.oss.digiwf.cocreation.core.shared.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssignmentJpaRepository extends JpaRepository<AssignmentEntity, String> {

    List<AssignmentEntity> findAssignmentEntitiesByAssignmentId_UserIdEquals(String userId);

    Optional<AssignmentEntity> findByAssignmentId_RepositoryIdAndAssignmentId_UserId(String repositoryId, String userId);

    List<AssignmentEntity> findByAssignmentId_UserIdAndRoleIn(String userId, List<RoleEnum> roles);

    List<AssignmentEntity> findByAssignmentId_RepositoryId(String repositoryId);

    int countByAssignmentId_RepositoryId(String repositoryId);

    void deleteAssignmentEntityByAssignmentId_RepositoryIdAndAssignmentId_UserId(String repositoryId, String userId);

    int deleteAllByAssignmentId_RepositoryId(String repositoryId);

}
