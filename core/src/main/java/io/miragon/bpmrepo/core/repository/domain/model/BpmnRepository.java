package io.miragon.bpmrepo.core.repository.domain.model;

import io.miragon.bpmrepo.core.repository.api.transport.NewBpmnRepositoryTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class BpmnRepository {

    private final String bpmnRepositoryId;
    private String bpmnRepositoryName;
    private String bpmnRepositoryDescription;
    private final LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Integer existingDiagrams;
    private Integer assignedUsers;

    public void update(final NewBpmnRepositoryTO newBpmnRepositoryTO) {
        if (newBpmnRepositoryTO.getBpmnRepositoryName() != null && !newBpmnRepositoryTO.getBpmnRepositoryName().isEmpty()) {
            this.bpmnRepositoryName = newBpmnRepositoryTO.getBpmnRepositoryName();
        }
        if (newBpmnRepositoryTO.getBpmnRepositoryDescription() != null && !newBpmnRepositoryTO.getBpmnRepositoryDescription().isEmpty()) {
            this.bpmnRepositoryDescription = newBpmnRepositoryTO.getBpmnRepositoryDescription();
        }
        this.updatedDate = LocalDateTime.now();
    }

    public void updateAssingedUsers(final Integer users) {
        this.assignedUsers = users;
    }

    public void updateExistingDiagrams(final Integer existingDiagrams) {
        this.existingDiagrams = existingDiagrams;
    }

}
