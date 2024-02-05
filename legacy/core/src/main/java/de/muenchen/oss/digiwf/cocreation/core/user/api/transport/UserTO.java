package de.muenchen.oss.digiwf.cocreation.core.user.api.transport;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Simplified user object only containing username")
public class UserTO {

    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

}
