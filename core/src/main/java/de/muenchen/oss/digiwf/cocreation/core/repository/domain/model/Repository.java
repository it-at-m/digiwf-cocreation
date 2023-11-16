package de.muenchen.oss.digiwf.cocreation.core.repository.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class Repository {

    private String id;
    private String name;
    private String description;
    private final LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Integer existingArtifacts;
    private Integer assignedUsers;

    public Repository(final NewRepository newRepository) {
        this.name = newRepository.getName();
        this.description = newRepository.getDescription();
        this.existingArtifacts = 0;
        this.assignedUsers = 1;
        this.createdDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
    }

    public void update(final RepositoryUpdate repository) {
        if (StringUtils.isNotBlank(repository.getName())) {
            this.name = repository.getName();
        }
        if (StringUtils.isNotBlank(repository.getDescription())) {
            this.description = repository.getDescription();
        }
        this.updatedDate = LocalDateTime.now();
    }

    public void updateAssingedUsers(final Integer users) {
        this.assignedUsers = users;
    }

    public void updateExistingArtifacts(final Integer existingArtifacts) {
        this.existingArtifacts = existingArtifacts;
    }

}
