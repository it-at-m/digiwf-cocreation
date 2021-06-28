package io.miragon.bpmrepo.core.repository.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class RepositoryUpdate {

    private final String name;

    private final String description;
}
