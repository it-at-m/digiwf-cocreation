package io.miragon.bpmrepo.core.artifact.infrastructure.repository;

import io.miragon.bpmrepo.core.artifact.infrastructure.entity.StarredEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StarredJpaRepository extends JpaRepository<StarredEntity, String> {

    int deleteById_artifactIdAndId_UserId(String bpmnartifactId, String userId);

    StarredEntity findById_artifactIdAndId_UserId(String bpmnartifactId, String userId);

    List<StarredEntity> findAllById_UserId(String userId);

}
