package de.muenchen.oss.digiwf.cocreation.core.repository.api.transport;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Client created object for creating a new repository")
public class NewRepositoryTO {

    @NotBlank
    private String name;

    @NotNull
    private String description;

}
