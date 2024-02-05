package de.muenchen.oss.digiwf.cocreation.core.repository.api.transport;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepositoryUpdateTO {

    @NotBlank
    private String name;

    @NotNull
    private String description;

}
