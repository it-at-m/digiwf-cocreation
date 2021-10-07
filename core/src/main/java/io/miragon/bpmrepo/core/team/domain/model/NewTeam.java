package io.miragon.bpmrepo.core.team.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class NewTeam {

    private final String name;

    private final String description;
}
